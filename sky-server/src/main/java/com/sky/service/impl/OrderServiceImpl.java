package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.*;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO dto) {
        Long userId = BaseContext.getCurrentId();

        List<ShoppingCart> cartList = shoppingCartMapper.listByUserId(userId);
        if (cartList == null || cartList.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.CART_IS_NULL);
        }

        AddressBook addressBook = addressBookMapper.getById(dto.getAddressBookId());
        if (addressBook == null) {
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        User user = userMapper.getById(userId);

        Orders order = new Orders();
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setUserId(userId);
        order.setAddressBookId(dto.getAddressBookId());
        order.setOrderTime(LocalDateTime.now());
        order.setPayMethod(dto.getPayMethod());
        order.setPayStatus(Orders.UN_PAID);
        order.setAmount(dto.getAmount());
        order.setRemark(dto.getRemark());
        order.setPhone(dto.getPhone() != null ? dto.getPhone() : addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setUserName(user.getName());
        order.setConsignee(addressBook.getConsignee());
        order.setDeliveryStatus(0);
        order.setPackAmount(dto.getPackAmount());
        order.setTablewareNumber(dto.getTablewareNumber());
        order.setTablewareStatus(dto.getTablewareStatus());

        orderMapper.insert(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart cart : cartList) {
            OrderDetail detail = new OrderDetail();
            BeanUtils.copyProperties(cart, detail);
            detail.setOrderId(order.getId());
            orderDetails.add(detail);
            orderDetailMapper.insert(detail);
        }

        shoppingCartMapper.deleteByUserId(userId);

        OrderSubmitVO vo = OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();

        // WebSocket notification to admin
        webSocketServer.sendToAllClient("新订单提醒: 订单号" + order.getNumber());

        return vo;
    }

    @Override
    public PageResult pageQuery(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Long userId = BaseContext.getCurrentId();
        Page<Orders> page = (Page<Orders>) orderMapper.pageQuery(
                userId, dto.getStatus(), dto.getNumber(), dto.getPhone(),
                dto.getBeginTime(), dto.getEndTime());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public OrderVO getOrderDetail(Long id) {
        Orders order = orderMapper.getById(id);
        List<OrderDetail> details = orderDetailMapper.getByOrderId(id);
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setOrderDetailList(details);
        return vo;
    }

    @Override
    public void cancel(Long id) {
        Orders order = orderMapper.getById(id);
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if (order.getStatus() > Orders.TO_BE_CONFIRMED) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        order.setStatus(Orders.CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        order.setCancelReason("用户取消");
        orderMapper.update(order);
    }

    @Override
    @Transactional
    public void reOrder(Long id) {
        Long userId = BaseContext.getCurrentId();
        List<OrderDetail> details = orderDetailMapper.getByOrderId(id);
        for (OrderDetail detail : details) {
            ShoppingCart cart = new ShoppingCart();
            BeanUtils.copyProperties(detail, cart);
            cart.setUserId(userId);
            cart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(cart);
        }
    }

    @Override
    public void reminder(Long id) {
        Orders order = orderMapper.getById(id);
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        webSocketServer.sendToAllClient("用户催单: 订单号" + order.getNumber());
    }

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<Orders> page = (Page<Orders>) orderMapper.conditionSearch(
                dto.getStatus(), dto.getNumber(), dto.getPhone(),
                dto.getBeginTime(), dto.getEndTime());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public OrderStatisticsVO statistics() {
        Integer toBeConfirmed = orderMapper.countByStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countByStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countByStatus(Orders.DELIVERY_IN_PROGRESS);
        OrderStatisticsVO vo = new OrderStatisticsVO();
        vo.setToBeConfirmed(toBeConfirmed);
        vo.setConfirmed(confirmed);
        vo.setDeliveryInProgress(deliveryInProgress);
        return vo;
    }

    @Override
    public void confirm(OrdersConfirmDTO dto) {
        Orders order = orderMapper.getById(dto.getId());
        if (order == null || !Orders.TO_BE_CONFIRMED.equals(order.getStatus())) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        order.setStatus(Orders.CONFIRMED);
        orderMapper.update(order);
    }

    @Override
    public void rejection(OrdersRejectionDTO dto) {
        Orders order = orderMapper.getById(dto.getId());
        if (order == null || !Orders.TO_BE_CONFIRMED.equals(order.getStatus())) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        order.setStatus(Orders.CANCELLED);
        order.setRejectionReason(dto.getRejectionReason());
        order.setCancelTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    public void cancelOrder(OrdersCancelDTO dto) {
        Orders order = orderMapper.getById(dto.getId());
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        order.setStatus(Orders.CANCELLED);
        order.setCancelReason(dto.getCancelReason());
        order.setCancelTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    public void delivery(Long id) {
        Orders order = orderMapper.getById(id);
        if (order == null || !Orders.CONFIRMED.equals(order.getStatus())) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        order.setStatus(Orders.DELIVERY_IN_PROGRESS);
        order.setDeliveryStatus(1);
        order.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    public void complete(Long id) {
        Orders order = orderMapper.getById(id);
        if (order == null || !Orders.DELIVERY_IN_PROGRESS.equals(order.getStatus())) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        order.setStatus(Orders.COMPLETED);
        order.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(order);
    }
}

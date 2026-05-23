package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?")
    public void cancelUnpaidOrders() {
        log.info("定时任务: 取消超时未支付订单");
        LocalDateTime deadline = LocalDateTime.now().minusMinutes(15);
        List<Orders> unpaidOrders = orderMapper.findByStatusAndOrderTimeBefore(
                Orders.PENDING_PAYMENT, deadline);
        if (unpaidOrders != null && !unpaidOrders.isEmpty()) {
            for (Orders order : unpaidOrders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("超时未支付，自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
            log.info("取消了 {} 个超时未支付订单", unpaidOrders.size());
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void autoCompleteOrders() {
        log.info("定时任务: 自动完成配送超时订单");
        LocalDateTime deadline = LocalDateTime.now().minusHours(1);
        List<Orders> deliveringOrders = orderMapper.findByStatusAndOrderTimeBefore(
                Orders.DELIVERY_IN_PROGRESS, deadline);
        if (deliveringOrders != null && !deliveringOrders.isEmpty()) {
            for (Orders order : deliveringOrders) {
                order.setStatus(Orders.COMPLETED);
                order.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }
}

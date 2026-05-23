package com.sky.controller.user;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Tag(name = "C端订单管理")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @Operation(summary = "提交订单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO dto) {
        log.info("提交订单");
        return Result.success(orderService.submit(dto));
    }

    @GetMapping("/historyOrders")
    @Operation(summary = "历史订单")
    public Result<PageResult> historyOrders(OrdersPageQueryDTO dto) {
        return Result.success(orderService.pageQuery(dto));
    }

    @GetMapping("/orderDetail/{id}")
    @Operation(summary = "订单详情")
    public Result<OrderVO> orderDetail(@PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(id));
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "取消订单")
    public Result<?> cancel(@PathVariable Long id) {
        orderService.cancel(id);
        return Result.success();
    }

    @PostMapping("/reOrder/{id}")
    @Operation(summary = "再来一单")
    public Result<?> reOrder(@PathVariable Long id) {
        orderService.reOrder(id);
        return Result.success();
    }

    @GetMapping("/reminder/{id}")
    @Operation(summary = "客户催单")
    public Result<?> reminder(@PathVariable Long id) {
        orderService.reminder(id);
        return Result.success();
    }
}

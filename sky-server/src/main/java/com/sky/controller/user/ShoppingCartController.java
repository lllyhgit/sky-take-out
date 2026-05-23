package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Tag(name = "C端购物车")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @Operation(summary = "加入购物车")
    public Result<?> add(@RequestBody ShoppingCartDTO dto) {
        shoppingCartService.add(dto);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "查询购物车")
    public Result<List<ShoppingCart>> list() {
        return Result.success(shoppingCartService.list());
    }

    @DeleteMapping("/clean")
    @Operation(summary = "清空购物车")
    public Result<?> clean() {
        shoppingCartService.clean();
        return Result.success();
    }

    @PostMapping("/sub")
    @Operation(summary = "减少购物车商品")
    public Result<?> sub(@RequestBody ShoppingCartDTO dto) {
        shoppingCartService.sub(dto);
        return Result.success();
    }
}

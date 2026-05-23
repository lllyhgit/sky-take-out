package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/shop")
@Tag(name = "C端店铺操作")
public class ShopController {

    @GetMapping("/status")
    @Operation(summary = "查询店铺营业状态")
    public Result<Integer> status() {
        return Result.success(1);
    }
}

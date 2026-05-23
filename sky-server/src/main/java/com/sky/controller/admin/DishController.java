package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Tag(name = "菜品管理")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @Operation(summary = "新增菜品")
    public Result<?> save(@RequestBody DishDTO dto) {
        log.info("新增菜品: {}", dto.getName());
        dishService.save(dto);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询菜品")
    public Result<PageResult> page(DishPageQueryDTO dto) {
        return Result.success(dishService.pageQuery(dto));
    }

    @DeleteMapping
    @Operation(summary = "删除菜品")
    public Result<?> delete(@RequestParam List<Long> ids) {
        dishService.delete(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询菜品详情")
    public Result<DishVO> getById(@PathVariable Long id) {
        return Result.success(dishService.getById(id));
    }

    @PutMapping
    @Operation(summary = "编辑菜品")
    public Result<?> update(@RequestBody DishDTO dto) {
        dishService.update(dto);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "起售/停售菜品")
    public Result<?> startOrStop(@PathVariable Integer status, Long id) {
        dishService.startOrStop(status, id);
        return Result.success();
    }
}

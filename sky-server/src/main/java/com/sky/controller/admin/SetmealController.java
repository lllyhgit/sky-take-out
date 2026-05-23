package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@Tag(name = "套餐管理")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @Operation(summary = "新增套餐")
    public Result<?> save(@RequestBody SetmealDTO dto) {
        setmealService.save(dto);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询套餐")
    public Result<PageResult> page(SetmealPageQueryDTO dto) {
        return Result.success(setmealService.pageQuery(dto));
    }

    @DeleteMapping
    @Operation(summary = "删除套餐")
    public Result<?> delete(@RequestParam List<Long> ids) {
        setmealService.delete(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询套餐详情")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        return Result.success(setmealService.getById(id));
    }

    @PutMapping
    @Operation(summary = "编辑套餐")
    public Result<?> update(@RequestBody SetmealDTO dto) {
        setmealService.update(dto);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "起售/停售套餐")
    public Result<?> startOrStop(@PathVariable Integer status, Long id) {
        setmealService.startOrStop(status, id);
        return Result.success();
    }
}

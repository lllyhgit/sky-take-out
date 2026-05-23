package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Tag(name = "分类管理")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @Operation(summary = "新增分类")
    public Result<?> save(@RequestBody CategoryDTO dto) {
        categoryService.save(dto);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询分类")
    public Result<PageResult> page(CategoryPageQueryDTO dto) {
        return Result.success(categoryService.pageQuery(dto));
    }

    @DeleteMapping
    @Operation(summary = "删除分类")
    public Result<?> deleteById(Long id) {
        categoryService.deleteById(id);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "编辑分类")
    public Result<?> update(@RequestBody CategoryDTO dto) {
        categoryService.update(dto);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "启用/禁用分类")
    public Result<?> startOrStop(@PathVariable Integer status, Long id) {
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "查询分类列表")
    public Result<List<Category>> list(Integer type) {
        return Result.success(categoryService.list(type));
    }
}

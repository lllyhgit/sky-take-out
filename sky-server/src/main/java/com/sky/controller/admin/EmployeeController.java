package com.sky.controller.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/employee")
@Tag(name = "员工管理")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    @Operation(summary = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO dto) {
        log.info("员工登录: {}", dto.getUsername());
        EmployeeLoginVO vo = employeeService.login(dto);
        return Result.success(vo);
    }

    @PostMapping("/logout")
    @Operation(summary = "员工登出")
    public Result<?> logout() {
        employeeService.logout();
        return Result.success();
    }

    @PostMapping
    @Operation(summary = "新增员工")
    public Result<?> save(@RequestBody EmployeeDTO dto) {
        log.info("新增员工: {}", dto.getUsername());
        employeeService.save(dto);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询员工")
    public Result<PageResult> page(EmployeePageQueryDTO dto) {
        log.info("分页查询员工: page={}, pageSize={}", dto.getPage(), dto.getPageSize());
        return Result.success(employeeService.pageQuery(dto));
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "启用/禁用员工")
    public Result<?> startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用/禁用员工: id={}, status={}", id, status);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询员工详情")
    public Result<Employee> getById(@PathVariable("id") Long id) {
        return Result.success(employeeService.getById(id));
    }

    @PutMapping
    @Operation(summary = "编辑员工")
    public Result<?> update(@RequestBody EmployeeDTO dto) {
        employeeService.update(dto);
        return Result.success();
    }

    @PutMapping("/editPassword")
    @Operation(summary = "修改密码")
    public Result<?> editPassword(@RequestBody PasswordEditDTO dto) {
        employeeService.editPassword(dto);
        return Result.success();
    }
}

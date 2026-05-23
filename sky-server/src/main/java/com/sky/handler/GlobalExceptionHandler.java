package com.sky.handler;

import com.sky.exception.*;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.sql.SQLIntegrityConstraintViolationException;
// 尝试做出修改
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public Result<?> accountNotFoundHandler(AccountNotFoundException ex) {
        log.error("账号不存在: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(PasswordErrorException.class)
    public Result<?> passwordErrorHandler(PasswordErrorException ex) {
        log.error("密码错误: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(AccountLockedException.class)
    public Result<?> accountLockedHandler(AccountLockedException ex) {
        log.error("账号锁定: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(LoginFailedException.class)
    public Result<?> loginFailedHandler(LoginFailedException ex) {
        log.error("登录失败: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result<?> businessExceptionHandler(BusinessException ex) {
        log.error("业务异常: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(DeletionNotAllowedException.class)
    public Result<?> deletionNotAllowedHandler(DeletionNotAllowedException ex) {
        log.error("不允许删除: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(OrderBusinessException.class)
    public Result<?> orderBusinessHandler(OrderBusinessException ex) {
        log.error("订单异常: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(ShoppingCartBusinessException.class)
    public Result<?> shoppingCartHandler(ShoppingCartBusinessException ex) {
        log.error("购物车异常: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<?> sqlIntegrityHandler(SQLIntegrityConstraintViolationException ex) {
        String msg = ex.getMessage();
        if (msg.contains("Duplicate entry")) {
            String[] parts = msg.split(" ");
            String value = parts.length > 2 ? parts[2] : "未知";
            return Result.error(value + " 已存在");
        }
        return Result.error("数据操作异常");
    }

    @ExceptionHandler(Exception.class)
    public Result<?> exceptionHandler(Exception ex) {
        log.error("系统异常: ", ex);
        return Result.error("系统异常，请联系管理员");
    }
}
     
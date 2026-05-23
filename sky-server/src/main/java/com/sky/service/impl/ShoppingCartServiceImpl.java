package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void add(ShoppingCartDTO dto) {
        Long userId = BaseContext.getCurrentId();

        if (dto.getDishId() != null) {
            ShoppingCart existing = shoppingCartMapper.getByUserIdAndDishIdAndFlavor(
                    userId, dto.getDishId(), dto.getDishFlavor());
            if (existing != null) {
                existing.setNumber(existing.getNumber() + 1);
                Dish dish = dishMapper.getById(dto.getDishId());
                existing.setAmount(dish.getPrice().multiply(BigDecimal.valueOf(existing.getNumber())));
                shoppingCartMapper.updateNumberAndAmount(existing);
                return;
            }

            Dish dish = dishMapper.getById(dto.getDishId());
            ShoppingCart cart = new ShoppingCart();
            cart.setUserId(userId);
            cart.setDishId(dto.getDishId());
            cart.setDishFlavor(dto.getDishFlavor());
            cart.setName(dish.getName());
            cart.setImage(dish.getImage());
            cart.setNumber(1);
            cart.setAmount(dish.getPrice());
            cart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(cart);
        } else if (dto.getSetmealId() != null) {
            ShoppingCart existing = shoppingCartMapper.getByUserIdAndSetmealId(
                    userId, dto.getSetmealId());
            if (existing != null) {
                existing.setNumber(existing.getNumber() + 1);
                Setmeal setmeal = setmealMapper.getById(dto.getSetmealId());
                existing.setAmount(setmeal.getPrice().multiply(BigDecimal.valueOf(existing.getNumber())));
                shoppingCartMapper.updateNumberAndAmount(existing);
                return;
            }

            Setmeal setmeal = setmealMapper.getById(dto.getSetmealId());
            ShoppingCart cart = new ShoppingCart();
            cart.setUserId(userId);
            cart.setSetmealId(dto.getSetmealId());
            cart.setName(setmeal.getName());
            cart.setImage(setmeal.getImage());
            cart.setNumber(1);
            cart.setAmount(setmeal.getPrice());
            cart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(cart);
        }
    }

    @Override
    public List<ShoppingCart> list() {
        return shoppingCartMapper.listByUserId(BaseContext.getCurrentId());
    }

    @Override
    public void clean() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }

    @Override
    public void sub(ShoppingCartDTO dto) {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart existing;
        if (dto.getDishId() != null) {
            existing = shoppingCartMapper.getByUserIdAndDishIdAndFlavor(
                    userId, dto.getDishId(), dto.getDishFlavor());
        } else {
            existing = shoppingCartMapper.getByUserIdAndSetmealId(
                    userId, dto.getSetmealId());
        }
        if (existing == null) {
            throw new ShoppingCartBusinessException("购物车中没有该商品");
        }
        if (existing.getNumber() <= 1) {
            shoppingCartMapper.deleteById(existing.getId());
        } else {
            shoppingCartMapper.decreaseNumber(existing.getId());
        }
    }
}

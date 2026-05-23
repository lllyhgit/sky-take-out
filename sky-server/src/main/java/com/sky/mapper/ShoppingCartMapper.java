package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    @Select("SELECT * FROM shopping_cart WHERE user_id = #{userId}")
    List<ShoppingCart> listByUserId(Long userId);

    @Insert("INSERT INTO shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "VALUES (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ShoppingCart shoppingCart);

    @Update("UPDATE shopping_cart SET number = #{number}, amount = #{amount} WHERE id = #{id}")
    void updateNumberAndAmount(ShoppingCart shoppingCart);

    @Delete("DELETE FROM shopping_cart WHERE user_id = #{userId}")
    void deleteByUserId(Long userId);

    @Delete("DELETE FROM shopping_cart WHERE id = #{id}")
    void deleteById(Long id);

    @Select("SELECT * FROM shopping_cart WHERE user_id = #{userId} AND dish_id = #{dishId} AND dish_flavor = #{dishFlavor}")
    ShoppingCart getByUserIdAndDishIdAndFlavor(Long userId, Long dishId, String dishFlavor);

    @Select("SELECT * FROM shopping_cart WHERE user_id = #{userId} AND setmeal_id = #{setmealId}")
    ShoppingCart getByUserIdAndSetmealId(Long userId, Long setmealId);

    @Update("UPDATE shopping_cart SET number = number - 1, amount = amount - (amount / (number + 1)) WHERE id = #{id}")
    void decreaseNumber(Long id);
}

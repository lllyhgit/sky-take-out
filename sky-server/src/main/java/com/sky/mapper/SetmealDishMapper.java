package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    void insertBatch(List<SetmealDish> setmealDishes);

    @Select("DELETE FROM setmeal_dish WHERE setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    @Select("SELECT * FROM setmeal_dish WHERE setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

    @Select("SELECT * FROM setmeal_dish WHERE dish_id IN (SELECT dish_id FROM setmeal_dish WHERE setmeal_id IN (SELECT id FROM setmeal WHERE status = 1)) AND dish_id = #{dishId}")
    List<SetmealDish> findByDishIdInActiveSetmeals(Long dishId);
}

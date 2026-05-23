package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    void deleteById(Long id);

    @Select("SELECT * FROM dish WHERE id = #{id}")
    Dish getById(Long id);

    List<Dish> pageQuery(String name, Long categoryId, Integer status);

    @Select("SELECT * FROM dish WHERE category_id = #{categoryId} AND status = 1")
    List<Dish> listByCategoryId(Long categoryId);

    @Select("SELECT COUNT(*) FROM dish WHERE category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @Select("SELECT COUNT(*) FROM dish WHERE status = #{status}")
    Integer countByStatus(Integer status);
}

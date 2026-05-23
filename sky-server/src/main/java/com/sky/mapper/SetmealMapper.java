package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    void deleteById(Long id);

    @Select("SELECT * FROM setmeal WHERE id = #{id}")
    Setmeal getById(Long id);

    List<Setmeal> pageQuery(String name, Long categoryId, Integer status);

    @Select("SELECT * FROM setmeal WHERE category_id = #{categoryId} AND status = 1")
    List<Setmeal> listByCategoryId(Long categoryId);

    @Select("SELECT COUNT(*) FROM setmeal WHERE category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @Select("SELECT COUNT(*) FROM setmeal WHERE status = #{status}")
    Integer countByStatus(Integer status);
}

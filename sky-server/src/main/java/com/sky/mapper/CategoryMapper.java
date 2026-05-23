package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @AutoFill(OperationType.INSERT)
    void insert(Category category);

    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    void deleteById(Long id);

    @Select("SELECT * FROM category WHERE id = #{id}")
    Category getById(Long id);

    List<Category> pageQuery(Integer type, String name);

    @Select("SELECT * FROM category WHERE type = #{type}")
    List<Category> listByType(Integer type);

    @Select("SELECT * FROM category WHERE status = 1")
    List<Category> listAll();
}

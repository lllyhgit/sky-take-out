package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    @Select("SELECT * FROM employee WHERE username = #{username}")
    Employee getByUsername(String username);

    @Select("SELECT * FROM employee WHERE id = #{id}")
    Employee getById(Long id);

    List<Employee> pageQuery(String name);

    @AutoFill(OperationType.INSERT)
    void insert(Employee employee);

    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);
}

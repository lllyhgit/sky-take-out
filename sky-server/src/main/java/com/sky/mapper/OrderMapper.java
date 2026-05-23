package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    void insert(Orders order);

    void update(Orders order);

    @Select("SELECT * FROM orders WHERE id = #{id}")
    Orders getById(Long id);

    @Select("SELECT * FROM orders WHERE number = #{number}")
    Orders getByNumber(String number);

    List<Orders> pageQuery(Long userId, Integer status, String number, String phone,
                           String beginTime, String endTime);

    List<Orders> conditionSearch(Integer status, String number, String phone,
                                  String beginTime, String endTime);

    @Select("SELECT * FROM orders WHERE status = #{status} AND order_time < #{deadline}")
    List<Orders> findByStatusAndOrderTimeBefore(@Param("status") Integer status, @Param("deadline") LocalDateTime deadline);

    @Select("SELECT COUNT(*) FROM orders WHERE status = #{status}")
    Integer countByStatus(Integer status);

    @Update("UPDATE orders SET status = #{status} WHERE id = #{id}")
    void updateStatus(Long id, Integer status);

    Double getTurnoverByDate(Map<String, Object> params);

    Integer getOrderCountByDate(Map<String, Object> params);

    Integer getValidOrderCountByDate(Map<String, Object> params);

    List<Map<String, Object>> getTop10Dishes(LocalDate begin, LocalDate end);
}

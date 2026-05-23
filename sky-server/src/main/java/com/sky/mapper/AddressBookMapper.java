package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    @Insert("INSERT INTO address_book (user_id, consignee, sex, phone, province_code, province_name, " +
            "city_code, city_name, district_code, district_name, detail, label, is_default) VALUES " +
            "(#{userId}, #{consignee}, #{sex}, #{phone}, #{provinceCode}, #{provinceName}, " +
            "#{cityCode}, #{cityName}, #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(AddressBook addressBook);

    void update(AddressBook addressBook);

    @Delete("DELETE FROM address_book WHERE id = #{id}")
    void deleteById(Long id);

    @Select("SELECT * FROM address_book WHERE id = #{id}")
    AddressBook getById(Long id);

    @Select("SELECT * FROM address_book WHERE user_id = #{userId} ORDER BY is_default DESC")
    List<AddressBook> listByUserId(Long userId);

    @Update("UPDATE address_book SET is_default = 0 WHERE user_id = #{userId}")
    void clearDefault(Long userId);

    @Update("UPDATE address_book SET is_default = 1 WHERE id = #{id}")
    void setDefault(Long id);
}

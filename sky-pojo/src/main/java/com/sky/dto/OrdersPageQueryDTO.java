package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrdersPageQueryDTO implements Serializable {
    private Integer page;
    private Integer pageSize;
    private String number;
    private String phone;
    private Integer status;
    private String beginTime;
    private String endTime;
}

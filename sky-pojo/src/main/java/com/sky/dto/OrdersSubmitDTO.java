package com.sky.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrdersSubmitDTO implements Serializable {
    private Long addressBookId;
    private Integer payMethod;
    private BigDecimal amount;
    private String remark;
    private String phone;
    private String address;
    private String consignee;
    private Integer tablewareNumber;
    private Integer tablewareStatus;
    private Integer packAmount;
}

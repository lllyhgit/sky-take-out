package com.sky.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class DataOverViewQueryDTO implements Serializable {
    private LocalDate begin;
    private LocalDate end;
}

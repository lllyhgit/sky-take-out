package com.sky.service;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.vo.*;

import jakarta.servlet.http.HttpServletResponse;

public interface ReportService {

    TurnoverReportVO getTurnoverStatistics(DataOverViewQueryDTO dto);

    UserReportVO getUserStatistics(DataOverViewQueryDTO dto);

    OrderReportVO getOrderReport(DataOverViewQueryDTO dto);

    SalesTop10ReportVO getTop10(DataOverViewQueryDTO dto);

    void export(HttpServletResponse response);
}

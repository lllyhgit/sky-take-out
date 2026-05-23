package com.sky.controller.admin;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/report")
@Tag(name = "数据统计")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/turnoverStatistics")
    @Operation(summary = "营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(DataOverViewQueryDTO dto) {
        return Result.success(reportService.getTurnoverStatistics(dto));
    }

    @GetMapping("/userStatistics")
    @Operation(summary = "用户统计")
    public Result<UserReportVO> userStatistics(DataOverViewQueryDTO dto) {
        return Result.success(reportService.getUserStatistics(dto));
    }

    @GetMapping("/orderReport")
    @Operation(summary = "订单统计")
    public Result<OrderReportVO> orderReport(DataOverViewQueryDTO dto) {
        return Result.success(reportService.getOrderReport(dto));
    }

    @GetMapping("/top10")
    @Operation(summary = "销量TOP10")
    public Result<SalesTop10ReportVO> top10(DataOverViewQueryDTO dto) {
        return Result.success(reportService.getTop10(dto));
    }

    @GetMapping("/export")
    @Operation(summary = "导出Excel报表")
    public void export(HttpServletResponse response) {
        reportService.export(response);
    }
}

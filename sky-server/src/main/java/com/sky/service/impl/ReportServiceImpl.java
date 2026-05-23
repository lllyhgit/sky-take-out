package com.sky.service.impl;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public TurnoverReportVO getTurnoverStatistics(DataOverViewQueryDTO dto) {
        List<LocalDate> dates = getDateList(dto.getBegin(), dto.getEnd());
        List<Double> turnovers = new ArrayList<>();

        for (LocalDate date : dates) {
            Map<String, Object> params = new HashMap<>();
            params.put("date", date);
            Double turnover = orderMapper.getTurnoverByDate(params);
            turnovers.add(turnover != null ? turnover : 0.0);
        }

        return TurnoverReportVO.builder()
                .dateList(dates.stream().map(LocalDate::toString).collect(Collectors.joining(",")))
                .turnoverList(turnovers.stream().map(Object::toString).collect(Collectors.joining(",")))
                .build();
    }

    @Override
    public UserReportVO getUserStatistics(DataOverViewQueryDTO dto) {
        List<LocalDate> dates = getDateList(dto.getBegin(), dto.getEnd());
        List<Integer> newUsers = new ArrayList<>();
        List<Integer> totalUsers = new ArrayList<>();

        for (LocalDate date : dates) {
            newUsers.add(userMapper.countNewUsersByDate(date));
            totalUsers.add(userMapper.countTotalUsersByDate(date));
        }

        return UserReportVO.builder()
                .dateList(dates.stream().map(LocalDate::toString).collect(Collectors.joining(",")))
                .newUserList(newUsers.stream().map(Object::toString).collect(Collectors.joining(",")))
                .totalUserList(totalUsers.stream().map(Object::toString).collect(Collectors.joining(",")))
                .build();
    }

    @Override
    public OrderReportVO getOrderReport(DataOverViewQueryDTO dto) {
        List<LocalDate> dates = getDateList(dto.getBegin(), dto.getEnd());
        List<Integer> orderCounts = new ArrayList<>();
        List<Integer> validOrderCounts = new ArrayList<>();

        for (LocalDate date : dates) {
            Map<String, Object> params = new HashMap<>();
            params.put("date", date);
            orderCounts.add(orderMapper.getOrderCountByDate(params));
            validOrderCounts.add(orderMapper.getValidOrderCountByDate(params));
        }

        int totalOrders = orderCounts.stream().mapToInt(Integer::intValue).sum();
        int validOrders = validOrderCounts.stream().mapToInt(Integer::intValue).sum();
        double completionRate = totalOrders > 0 ? (double) validOrders / totalOrders : 0.0;

        return OrderReportVO.builder()
                .dateList(dates.stream().map(LocalDate::toString).collect(Collectors.joining(",")))
                .orderCountList(orderCounts.stream().map(Object::toString).collect(Collectors.joining(",")))
                .validOrderCountList(validOrderCounts.stream().map(Object::toString).collect(Collectors.joining(",")))
                .totalOrderCount(totalOrders)
                .validOrderCount(validOrders)
                .orderCompletionRate(completionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getTop10(DataOverViewQueryDTO dto) {
        List<Map<String, Object>> top10 = orderMapper.getTop10Dishes(dto.getBegin(), dto.getEnd());
        List<String> names = new ArrayList<>();
        List<String> counts = new ArrayList<>();

        for (Map<String, Object> item : top10) {
            names.add((String) item.get("name"));
            counts.add(item.get("count").toString());
        }

        return SalesTop10ReportVO.builder()
                .nameList(String.join(",", names))
                .numberList(String.join(",", counts))
                .build();
    }

    @Override
    public void export(HttpServletResponse response) {
        LocalDate end = LocalDate.now().minusDays(1);
        LocalDate begin = end.minusDays(30);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("运营数据报表");

            XSSFRow header = sheet.createRow(0);
            header.createCell(0).setCellValue("统计时间区间");
            header.createCell(1).setCellValue(begin + " 至 " + end);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=report.xlsx");
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("导出Excel失败", e);
        }
    }

    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        long days = ChronoUnit.DAYS.between(begin, end);
        for (long i = 0; i <= days; i++) {
            dates.add(begin.plusDays(i));
        }
        return dates;
    }
}

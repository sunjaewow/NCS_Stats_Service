package com.example.excel;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class ExcelExportController {

    private final DBConnection dbConnection;

    @GetMapping("/export-excel")
    public void exportExcel(@RequestParam("content_id") int contentId, HttpServletResponse response) {
        String query = "SELECT start_time, ad_cnt, cd_cnt FROM schedule WHERE content_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstm = conn.prepareStatement(query)) {

            pstm.setInt(1, contentId);
            ResultSet rs = pstm.executeQuery();

            Workbook workbook = new XSSFWorkbook();

            Map<YearMonth, Map<LocalDate, Map<String, int[]>>> data = new TreeMap<>();
            Set<String> reservationTime = new TreeSet<>();

            while (rs.next()) {
                LocalDateTime localDateTime = rs.getTimestamp("start_time").toLocalDateTime();
                LocalDate localDate = localDateTime.toLocalDate();
                String time = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                YearMonth month = YearMonth.from(localDate);
                int adultCount = rs.getInt("ad_cnt");
                int childCount = rs.getInt("cd_cnt");

                data
                        .computeIfAbsent(month, k -> new TreeMap<>())
                        .computeIfAbsent(localDate, k -> new TreeMap<>())
                        .put(time, new int[]{adultCount, childCount});

                reservationTime.add(time);
            }

            for (YearMonth month : data.keySet()) {
                Sheet sheet = workbook.createSheet(month.toString());
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("예약 날짜");

                List<String> reservationTimeList = new ArrayList<>(reservationTime);

                for (int i = 0; i < reservationTimeList.size(); i++) {
                    headerRow.createCell(i * 2 + 1).setCellValue(reservationTimeList.get(i));
                    sheet.setColumnWidth(i * 2 + 1, 4000);
                    sheet.setColumnWidth(i * 2 + 2, 4000);
                }
                sheet.setColumnWidth(0, 4000);

                Row subRow = sheet.createRow(1);
                subRow.createCell(0).setCellValue("");
                for (int i = 0; i < reservationTimeList.size(); i++) {
                    subRow.createCell(i * 2 + 1).setCellValue("대인");
                    subRow.createCell(i * 2 + 2).setCellValue("소인");
                }

                int rowNum = 2;
                for (int day = 1; day <= month.lengthOfMonth(); day++) {
                    LocalDate yearMonthDate = month.atDay(day);
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(yearMonthDate.toString());

                    Map<String, int[]> dailyReservation = data.get(month).getOrDefault(yearMonthDate, new TreeMap<>());
                    for (int i = 0; i < reservationTimeList.size(); i++) {
                        int[] counts = dailyReservation.getOrDefault(reservationTimeList.get(i), new int[]{0, 0});
                        row.createCell(i * 2 + 1).setCellValue(counts[0]);
                        row.createCell(i * 2 + 2).setCellValue(counts[1]);
                    }
                }
            }

            response.setContentType("application/vnd.malformations-officedocument.spreadsheet.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=reservations.xlsx");

            workbook.write(response.getOutputStream());
            workbook.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

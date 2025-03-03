package com.example.excel.service;

import com.example.excel.domain.Schedule;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class ScheduleService {

    public Workbook createExcel(Schedule schedule) {
        Workbook workbook = new XSSFWorkbook();
        for (YearMonth month : schedule.getData().keySet()) {
            Sheet sheet = workbook.createSheet(month.toString());
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("예약 날짜");

            List<String> reservationsTimeList = new ArrayList<>(schedule.getReservationsTime());

            createSheetHeader(sheet, headerRow, reservationsTimeList);

            int rowNum=2;
            fillSheetData(schedule, month, sheet, reservationsTimeList, rowNum);
        }
        return workbook;
    }

    private static void createSheetHeader(Sheet sheet, Row headerRow, List<String> reservationsTimeList) {
        Row row = sheet.createRow(1);
        for (int i = 0; i < reservationsTimeList.size(); i++) {
            headerRow.createCell(i * 2 + 1).setCellValue(reservationsTimeList.get(i));
            row.createCell(i * 2 + 1).setCellValue("대인");
            row.createCell(i * 2 + 2).setCellValue("소인");
            sheet.setColumnWidth(i * 2 + 1, 4000);
            sheet.setColumnWidth(i * 2 + 2, 4000);
        }
        sheet.setColumnWidth(0, 4000);
    }

    private static void fillSheetData(Schedule schedule, YearMonth month, Sheet sheet, List<String> reservationsTimeList, int rowNum) {
        for (int i = 1; i <= month.lengthOfMonth(); i++) {
            LocalDate localDate = month.atDay(i);
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(localDate.toString());

            Map<String, int[]> timeSlotCount = schedule.getData().get(month).getOrDefault(localDate, new TreeMap<>());
            for (int j = 0; j < schedule.getReservationsTime().size(); j++) {
                int[] count = timeSlotCount.getOrDefault(reservationsTimeList.get(j), new int[]{0, 0});
                row.createCell(j * 2 + 1).setCellValue(count[0]);
                row.createCell(j * 2 + 2).setCellValue(count[1]);
            }
        }
    }

}

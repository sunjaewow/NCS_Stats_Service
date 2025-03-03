package com.example.excel.controller;

import com.example.excel.domain.Schedule;
import com.example.excel.repository.ScheduleRepository;
import com.example.excel.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;

    @GetMapping()
    public  void exportExcel(@RequestParam int contentId, HttpServletResponse response) {
        Schedule data = scheduleRepository.findByContentId(contentId);
        Workbook workbook = scheduleService.createExcel(data);
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=reservations.xlsx");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

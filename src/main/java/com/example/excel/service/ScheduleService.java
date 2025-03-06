package com.example.excel.service;

import com.example.excel.domain.Schedule;
import com.example.excel.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ExcelService excelService;

    public Workbook createExcel(int contentId) {
        Schedule data = scheduleRepository.findByContentId(contentId);
        return excelService.createExcel(data);
    }

}

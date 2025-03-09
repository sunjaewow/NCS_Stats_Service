package com.example.excel.service;

import com.example.excel.domain.Schedule;
import com.example.excel.dto.GrpcResponse;
import com.example.excel.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GrpcClientService grpcClientService;
    private final ExcelService excelService;

    public Workbook createExcel(String startTime, int contentId) {
//        Schedule data = scheduleRepository.findByContentId(contentId);
        List<GrpcResponse> grpcResponses = grpcClientService.sendMessage(startTime, contentId);

        // Schedule 객체 생성
        Schedule schedule = new Schedule();

        for (GrpcResponse grpcResponse : grpcResponses) {
            // startTime을 LocalDate로 변환
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(grpcResponse.getStartTime(), formatter);

            LocalDate date = dateTime.toLocalDate();
            YearMonth yearMonth = YearMonth.from(dateTime);

            // 데이터 저장
            schedule.getData()
                    .computeIfAbsent(yearMonth, ym -> new TreeMap<>())
                    .computeIfAbsent(date, d -> new TreeMap<>())
                    .put(String.valueOf(contentId), new int[]{grpcResponse.getAdCnt(), grpcResponse.getCdCnt()});

            // 예약 시간 추가
            schedule.getReservationsTime().add(grpcResponse.getStartTime());
        }

        // Excel 생성
        return excelService.createExcel(schedule);
    }

}

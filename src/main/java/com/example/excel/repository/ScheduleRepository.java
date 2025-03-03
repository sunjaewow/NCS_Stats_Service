package com.example.excel.repository;

import com.example.excel.domain.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

@Repository
@RequiredArgsConstructor
public class ScheduleRepository {

    private final DataSource dataSource;
    public Schedule findByContentId(int contentId) {
        Schedule schedule = new Schedule();
        String query = "select start_time, ad_cnt, cd_cnt from schedule where content_id=?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(query)
        ) {
            pstm.setInt(1, contentId);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                LocalDateTime localDateTime = rs.getTimestamp("start_time").toLocalDateTime();
                LocalDate localDate = localDateTime.toLocalDate();
                String time = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                YearMonth month = YearMonth.from(localDate);
                int adCnt = rs.getInt("ad_cnt");
                int cdCnt = rs.getInt("cd_cnt");

                schedule.getData()
                        .computeIfAbsent(month, k -> new TreeMap<>())
                        .computeIfAbsent(localDate, k -> new TreeMap<>())
                        .put(time, new int[]{adCnt, cdCnt});
                schedule.getReservationsTime().add(time);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return schedule;
    }
}

package com.example.excel.domain;

import lombok.Getter;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@Getter
public class Schedule {

    private final Map<YearMonth, Map<LocalDate, Map<String, int[]>>> data;

    private final Set<String> reservationsTime;

    public Schedule() {
        this.data = new TreeMap<>();
        this.reservationsTime = new TreeSet<>();
    }
}

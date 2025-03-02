package com.example.excel;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class DBConnection {

    private final DataSource dataSource;

    public Connection getConnection() throws SQLException {
        return DataSourceUtils.getConnection(dataSource);
    }
}

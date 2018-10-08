package com.exrates.inout.domain.main;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
public class Currency implements RowMapper<Currency> {

    private int id;
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    public Currency(int id) {
        this.id = id;
    }

    @Override
    public Currency mapRow(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}

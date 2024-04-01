package com.example.geodata.repository.bulk;

import com.example.geodata.entity.Country;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Collection;

@Repository
public class CountryBulkRepository implements BulkRepository<Country> {

    private final JdbcTemplate jdbcTemplate;

    public CountryBulkRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    @Override
    public void bulkInsert(Collection<Country> countries) {
        jdbcTemplate.batchUpdate("INSERT INTO countries (country_name, nationality, latitude, longitude) VALUES (?, ?, ?, ?)",
                countries,
                100,
                (PreparedStatement ps, Country country) -> {
                    ps.setString(1, country.getName());
                    ps.setString(2, country.getNationality());
                    ps.setDouble(3, country.getLatitude());
                    ps.setDouble(4, country.getLongitude());
                });
    }

}

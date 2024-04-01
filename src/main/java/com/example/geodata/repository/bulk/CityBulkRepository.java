package com.example.geodata.repository.bulk;

import com.example.geodata.entity.City;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Collection;

@Repository
public class CityBulkRepository implements BulkRepository<Integer, City> {

    private final JdbcTemplate jdbcTemplate;

    public CityBulkRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void bulkInsert(final Collection<City> cities) {
        jdbcTemplate.batchUpdate("INSERT INTO cities (city_name, fk_cities_countries, latitude, longitude) VALUES (?, ?, ?, ?)",
                cities,
                100,
                (PreparedStatement ps, City city) -> {
                    ps.setString(1, city.getName());
                    ps.setInt(2, city.getCountry().getId());
                    ps.setDouble(3, city.getLatitude());
                    ps.setDouble(4, city.getLongitude());
                });
    }

    @Override
    public void bulkUpdate(final Collection<City> entities) {

    }

    @Override
    public void bulkDelete(final Collection<Integer> ids) {

    }
}

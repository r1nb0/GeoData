package com.example.geodata.repository.bulk;

import com.example.geodata.entity.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Collection;

@Repository
public class LanguageBulkRepository implements BulkRepository<Integer, Language> {

    private final JdbcTemplate jdbcTemplate;

    public LanguageBulkRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void bulkInsert(Collection<Language> languages) {
        jdbcTemplate.batchUpdate("INSERT INTO languages (language_name, language_code) VALUES (?, ?)",
                languages,
                100,
                (PreparedStatement ps, Language language) -> {
                    ps.setString(1, language.getName());
                    ps.setString(2, language.getCode());
                });
    }

}

package com.epam.esm.dao.impl;

import com.epam.esm.config.Translator;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {
    private static final String SELECT_ALL = "SELECT id,name FROM tag";
    private static final String SELECT_BY_ID = "SELECT id,name  FROM tag WHERE id=?";
    private static final String INSERT = "INSERT INTO tag (id,name) VALUES (NULL,?)";
    private static final String REMOVE = "DELETE FROM tag WHERE id=?";
    private static final String SELECT_ALL_TAGS_FOR_CERTIFICATE = """
            SELECT id,name  FROM tag WHERE id IN (SELECT tag_id FROM certificates_has_tags WHERE gift_certificate_id = ?)
            """;
    private static final String SELECT_BY_NAME = "SELECT id,name  FROM tag WHERE name=?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @SneakyThrows(DaoException.class)
    public Number insert(Tag entity) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getName());
                return ps;
            }, keyHolder);
            return keyHolder.getKey();
        } catch (DataAccessException e) {
            throw new DaoException(Translator.toLocale("exception.insert.tag"));
        }
    }

    @Override
    @SneakyThrows({UnsupportedOperationException.class})
    public void update(Tag entity) {
        throw new UnsupportedOperationException(Translator.toLocale("Tag updates not supported"));
    }

    @Override
    public Optional<Tag> find(long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[]{id},
                    (rs, rowNum) -> Optional.of(new TagRowMapper().mapRow(rs, rowNum)));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> findAll() {
        try {
            return jdbcTemplate.query(SELECT_ALL, new TagRowMapper());
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public int remove(long id) {
        return jdbcTemplate.update(REMOVE, id);
    }

    @Override
    public List<Tag> findAllTagsByCertificateId(long id) {
        try {
            return jdbcTemplate.query(SELECT_ALL_TAGS_FOR_CERTIFICATE, new TagRowMapper(), id);
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Tag> findByName(String name) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_NAME, new Object[]{name},
                    (rs, rowNum) -> Optional.of(new TagRowMapper().mapRow(rs, rowNum)));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}

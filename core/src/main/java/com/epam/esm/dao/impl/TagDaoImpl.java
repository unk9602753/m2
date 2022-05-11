package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
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
    public Number insert(Tag entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getName());
            return ps;
        }, keyHolder);
        return keyHolder.getKey();
    }

    @Override
    public void update(Tag entity) {
        throw new UnsupportedOperationException("Unsupported operation for tag");
    }

    @Override
    public Optional<Tag> find(long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID,
                    (rs, rowNum) -> Optional.of(new TagRowMapper().mapRow(rs, rowNum)), id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new TagRowMapper());
    }

    @Override
    public int remove(long id) {
        return jdbcTemplate.update(REMOVE, id);
    }

    @Override
    public List<Tag> findAllTagsByCertificateId(long id) {
        return jdbcTemplate.query(SELECT_ALL_TAGS_FOR_CERTIFICATE, new TagRowMapper(), id);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_NAME,
                    (rs, rowNum) -> Optional.of(new TagRowMapper().mapRow(rs, rowNum)), name);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

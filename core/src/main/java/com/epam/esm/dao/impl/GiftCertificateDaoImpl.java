package com.epam.esm.dao.impl;

import com.epam.esm.config.Translator;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.mapper.GiftCertificateRowMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DaoException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String INSERT_TAG_IN_CERTIFICATE = """
            INSERT INTO certificates_has_tags (gift_certificate_id,tag_id) VALUES(?,?)""";
    private static final String DELETE_TAG_IN_CERTIFICATE = """
            DELETE FROM certificates_has_tags WHERE gift_certificate_id=?""";
    private static final String SELECT_ALL = "SELECT id,name ,description,price,duration,create_date,last_update_date FROM gift_certificate";
    private static final String SELECT_BY_ID = "SELECT id,name ,description,price,duration,create_date,last_update_date FROM gift_certificate WHERE id=?";
    private static final String INSERT = """
            INSERT INTO gift_certificate (id,name ,description,price,duration,create_date,last_update_date) 
            VALUES (NULL,?,?,?,?,?,?)""";
    private static final String UPDATE = """
            UPDATE gift_certificate SET name=? ,description=?,price=?,duration=?,create_date=?,last_update_date=?
            WHERE id=?""";
    private static final String SELECT_BY_PART_OF_NAME = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate WHERE name LIKE ?";
    private static final String REMOVE = "DELETE FROM gift_certificate WHERE id=?;";
    private static final String SELECT_BY_TAG_NAME = """
            SELECT id,name ,description,price,duration,create_date,last_update_date FROM gift_certificate WHERE id IN 
            (SELECT gift_certificate_id FROM tag JOIN certificates_has_tags ON tag.id=certificates_has_tags.tag_id AND name = ?)
            """;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @SneakyThrows(DaoException.class)
    public Number insert(GiftCertificate entity) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getName());
                ps.setString(2, entity.getDescription());
                ps.setDouble(3, entity.getPrice());
                ps.setInt(4, entity.getDuration());
                ps.setTimestamp(5, Timestamp.valueOf(entity.getCreateDate()));
                ps.setTimestamp(6, Timestamp.valueOf(entity.getLastUpdateDate()));
                return ps;
            }, keyHolder);
            return keyHolder.getKey();
        } catch (DataAccessException e) {
            throw new DaoException(Translator.toLocale("exception.insert.certificate"));
        }
    }

    @Override
    @SneakyThrows(DaoException.class)
    public void update(GiftCertificate entity) {
        try {
            jdbcTemplate.update(UPDATE, entity.getName(), entity.getDescription(), entity.getPrice(), entity.getDuration(),
                    entity.getCreateDate(), entity.getLastUpdateDate(), entity.getId());
        } catch (DataAccessException e) {
            throw new DaoException(Translator.toLocale("exception.update.certificate"));
        }
    }

    @Override
    public Optional<GiftCertificate> find(long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[]{id},
                    (rs, rowNum) -> Optional.of(new GiftCertificateRowMapper().mapRow(rs, rowNum)));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<GiftCertificate> findAll() {
        try {
            return jdbcTemplate.query(SELECT_ALL, new GiftCertificateRowMapper());
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public int remove(long id) {
        return jdbcTemplate.update(REMOVE, id);
    }

    @Override
    public List<GiftCertificate> findByCriteria(String criteria, String name) {
        try {
            if (criteria.equals("tag")) {
                return jdbcTemplate.query(SELECT_BY_TAG_NAME, new GiftCertificateRowMapper(), name);
            }
            return jdbcTemplate.query(SELECT_BY_PART_OF_NAME, new GiftCertificateRowMapper(), "%" + name + "%");
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<GiftCertificate> sort(String direction, String criteria) {
        String q = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate ORDER BY ";
        try {
            if (direction.equals("desc")) {
                return jdbcTemplate.query(q + criteria + " DESC", new GiftCertificateRowMapper());
            }
            return jdbcTemplate.query(q + criteria, new GiftCertificateRowMapper());
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    @SneakyThrows(DaoException.class)
    public long addTagToGiftCertificate(long giftCertificateId, long tagId) {
        try {
            return jdbcTemplate.update(INSERT_TAG_IN_CERTIFICATE, giftCertificateId, tagId);
        } catch (DataAccessException e) {
            throw new DaoException(Translator.toLocale("exception.add.tags.to.certificate") + giftCertificateId + " " + tagId);
        }
    }

    @Override
    @SneakyThrows(DaoException.class)
    public long removeTagToGiftCertificate(long id) {
        try {
            return jdbcTemplate.update(DELETE_TAG_IN_CERTIFICATE, id);
        } catch (DataAccessException e) {
            throw new DaoException(Translator.toLocale("exception.remove.tags.from.certificate" + id));
        }
    }
}
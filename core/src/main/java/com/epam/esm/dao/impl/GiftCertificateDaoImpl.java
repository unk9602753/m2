package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.mapper.GiftCertificateRowMapper;
import com.epam.esm.entity.GiftCertificate;
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
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String INSERT_TAG_IN_CERTIFICATE = """
            INSERT INTO certificates_has_tags (gift_certificate_id,tag_id) VALUES(?,?)""";
    private static final String DELETE_TAG_IN_CERTIFICATE = """
            DELETE FROM certificates_has_tags WHERE gift_certificate_id=?""";
    private static final String SELECT_ALL = "SELECT * FROM gift_certificate";
    private static final String SELECT_BY_ID = "SELECT * FROM gift_certificate WHERE id=?";
    private static final String INSERT = """
            INSERT INTO gift_certificate (id,name ,description,price,duration,create_date,last_update_date) 
            VALUES (NULL,?,?,?,?,?,?)""";
    private static final String UPDATE = """
            UPDATE gift_certificate SET name=? ,description=?,price=?,duration=?,create_date=?,last_update_date=?
            WHERE id=?""";
    private static final String SELECT_BY_PART_OF_NAME = "SELECT * FROM gift_certificate WHERE name LIKE ?";
    private static final String REMOVE = "DELETE FROM gift_certificate WHERE id=?;";
    private static final String ACS_SORT_BY_DATE = "SELECT * FROM gift_certificate ORDER BY create_date";
    private static final String DESC_SORT_BY_DATE = "SELECT * FROM gift_certificate ORDER BY create_date DESC ";
    private static final String SELECT_BY_TAG_NAME = """
            SELECT * FROM gift_certificate WHERE id IN 
            (SELECT gift_certificate_id FROM tag JOIN certificates_has_tags ON tag.id=certificates_has_tags.tag_id AND name = ?)
            """;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Number insert(GiftCertificate entity) {
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
    }

    @Override
    public long update(GiftCertificate entity) {
        jdbcTemplate.update(UPDATE, entity.getName(), entity.getDescription(), entity.getPrice(), entity.getDuration(),
                entity.getCreateDate(), entity.getLastUpdateDate(), entity.getId());
        return entity.getId();
    }

    @Override
    public Optional<GiftCertificate> find(long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[]{id},
                    (rs, rowNum) -> Optional.of(new GiftCertificateRowMapper().mapRow(rs, rowNum)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new GiftCertificateRowMapper());
    }

    @Override
    public long remove(long id) {
        return jdbcTemplate.update(REMOVE, id);
    }

    @Override
    public List<GiftCertificate> findByTagName(String tagName) {
        return jdbcTemplate.query(SELECT_BY_TAG_NAME, new GiftCertificateRowMapper(), tagName);
    }

    @Override
    public List<GiftCertificate> findByPartOfName(String giftCertificateName) throws DataAccessException {
        return jdbcTemplate.query(SELECT_BY_PART_OF_NAME, new GiftCertificateRowMapper(), "%" + giftCertificateName + "%");
    }

    @Override
    public List<GiftCertificate> ascSortByDate() {
        return jdbcTemplate.query(ACS_SORT_BY_DATE, new GiftCertificateRowMapper());
    }

    @Override
    public List<GiftCertificate> descSortByDate() throws DataAccessException {
        return jdbcTemplate.query(DESC_SORT_BY_DATE, new GiftCertificateRowMapper());
    }

    @Override
    public long addTagToGiftCertificate(long giftCertificateId, long tagId) throws DataAccessException {
        return jdbcTemplate.update(INSERT_TAG_IN_CERTIFICATE, giftCertificateId, tagId);
    }

    @Override
    public long removeTagToGiftCertificate(long giftCertificateId) {
        return jdbcTemplate.update(DELETE_TAG_IN_CERTIFICATE, giftCertificateId);
    }
}

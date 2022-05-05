package com.epam.esm.dao.mapper;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.dao.mapper.ColumnName.*;

public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {
    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GiftCertificate.builder()
                .id(rs.getLong(GIFT_CERTIFICATE_ID))
                .name(rs.getString(GIFT_CERTIFICATE_NAME))
                .description(rs.getString(GIFT_CERTIFICATE_DESCRIPTION))
                .price(rs.getDouble(GIFT_CERTIFICATE_PRICE))
                .duration(rs.getInt(GIFT_CERTIFICATE_DURATION))
                .createDate(rs.getTimestamp(GIFT_CERTIFICATE_CREATE_DATE).toLocalDateTime())
                .lastUpdateDate(rs.getTimestamp(GIFT_CERTIFICATE_LAST_UPDATE_DATE).toLocalDateTime())
                .build();
    }
}

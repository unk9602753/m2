package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao extends Dao<GiftCertificate> {
    List<GiftCertificate> findByTagName(String tagName);

    List<GiftCertificate> findByPartOfName(String giftCertificateName);

    List<GiftCertificate> sort(String direction, String criteria);

    long addTagToGiftCertificate(long giftCertificateId, long tagId);

    long removeTagToGiftCertificate(long giftCertificateId);
}

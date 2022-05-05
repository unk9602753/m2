package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao extends Dao<GiftCertificate> {
    List<GiftCertificate> findByTagName(String tagName);

    List<GiftCertificate> findByPartOfName(String giftCertificateName);

    List<GiftCertificate> ascSortByDate();

    List<GiftCertificate> descSortByDate();

    long addTagToGiftCertificate(long giftCertificateId, long tagId);

    long removeTagToGiftCertificate(long giftCertificateId);
}

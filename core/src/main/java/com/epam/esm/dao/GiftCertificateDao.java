package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao extends Dao<GiftCertificate> {

    List<GiftCertificate> findByCriteria(String criteria, String name);

    List<GiftCertificate> sort(String direction, String criteria);

    long addTagToGiftCertificate(long giftCertificateId, long tagId);

    long removeTagToGiftCertificate(long giftCertificateId);
}

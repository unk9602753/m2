package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService extends BaseService<GiftCertificateDto> {

    List<GiftCertificateDto> findByCriteria(String criteria, String name);

    List<GiftCertificateDto> findAllByCriteria(String direction, String criteria);

    void update(GiftCertificateDto giftCertificateDto, long id);
}

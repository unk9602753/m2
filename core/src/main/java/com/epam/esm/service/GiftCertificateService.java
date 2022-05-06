package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService extends BaseService<GiftCertificateDto> {
    List<GiftCertificateDto> findByTagName(String tagName);

    List<GiftCertificateDto> findByPartOfName(String giftCertificateName);

    List<GiftCertificateDto> sort(String direction, String criteria);

    void update(GiftCertificateDto giftCertificateDto, long id);
}

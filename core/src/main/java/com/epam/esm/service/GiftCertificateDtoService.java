package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificateDto;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDtoService extends BaseService<GiftCertificateDto> {
    List<GiftCertificateDto> findByTagName(String tagName);

    List<GiftCertificateDto> findByPartOfName(String giftCertificateName);

    List<GiftCertificateDto> ascSortByDate();

    List<GiftCertificateDto> descSortByDate();

    void update(GiftCertificateDto giftCertificateDto, long id);
}

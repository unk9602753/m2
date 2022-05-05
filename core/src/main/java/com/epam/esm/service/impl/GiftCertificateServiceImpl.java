package com.epam.esm.service.impl;

import com.epam.esm.config.Translator;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final String DOUBLE_REGEX = "^[1-9]+[0-9]*.\\d+";
    private static final String INTEGER_REGEX = "^[1-9]+[0-9]*$";
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    @Override
    @Transactional
    public Optional<GiftCertificateDto> delete(long id) {
        Optional<GiftCertificateDto> optCertificate = find(id);
        if (optCertificate.isPresent()) {
            giftCertificateDao.remove(id);
            return optCertificate;
        }
        return optCertificate;
    }

    @Override
    public Optional<GiftCertificateDto> find(long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateDao.find(id);
        List<Tag> allTagsByCertificateId = tagDao.findAllTagsByCertificateId(id);
        if (giftCertificate.isPresent()) {
            GiftCertificateDto giftCertificateDto = new GiftCertificateDto(giftCertificate.get());
            giftCertificateDto.setTags(allTagsByCertificateId);
            return Optional.of(giftCertificateDto);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> findAll() {
        List<GiftCertificateDto> dtoList = new ArrayList<>();
        List<GiftCertificate> all = giftCertificateDao.findAll();
        all.forEach(g -> {
            List<Tag> allTagsByCertificateId = tagDao.findAllTagsByCertificateId(g.getId());
            GiftCertificateDto giftCertificateDto = new GiftCertificateDto(g);
            giftCertificateDto.setTags(allTagsByCertificateId);
            dtoList.add(giftCertificateDto);
        });
        return dtoList;
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> findByTagName(String tagName) {
        List<GiftCertificateDto> dtoList = new ArrayList<>();
        List<GiftCertificate> giftCertificates = giftCertificateDao.findByTagName(tagName);
        giftCertificates.forEach(g -> {
            GiftCertificateDto giftCertificateDto = new GiftCertificateDto(g);
            List<Tag> allTagsByCertificateId = tagDao.findAllTagsByCertificateId(g.getId());
            giftCertificateDto.setTags(allTagsByCertificateId);
            dtoList.add(giftCertificateDto);
        });
        return dtoList;
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> findByPartOfName(String giftCertificateName) {
        List<GiftCertificateDto> dtoList = new ArrayList<>();
        List<GiftCertificate> giftCertificates = giftCertificateDao.findByPartOfName(giftCertificateName);
        giftCertificates.forEach(g -> {
            GiftCertificateDto giftCertificateDto = new GiftCertificateDto(g);
            List<Tag> allTagsByCertificateId = tagDao.findAllTagsByCertificateId(g.getId());
            giftCertificateDto.setTags(allTagsByCertificateId);
            dtoList.add(giftCertificateDto);
        });
        return dtoList;
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> ascSortByDate() {
        List<GiftCertificateDto> dtoList = new ArrayList<>();
        List<GiftCertificate> giftCertificates = giftCertificateDao.ascSortByDate();
        giftCertificates.forEach(g -> {
            GiftCertificateDto giftCertificateDto = new GiftCertificateDto(g);
            List<Tag> allTagsByCertificateId = tagDao.findAllTagsByCertificateId(g.getId());
            giftCertificateDto.setTags(allTagsByCertificateId);
            dtoList.add(giftCertificateDto);
        });
        return dtoList;
    }

    @Override
    @Transactional
    public Optional<GiftCertificateDto> create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = new GiftCertificate(giftCertificateDto);
        List<Tag> tags = giftCertificateDto.getTags();
        if (!String.valueOf(giftCertificate.getPrice()).matches(DOUBLE_REGEX)
                || !String.valueOf(giftCertificate.getDuration()).matches(INTEGER_REGEX)) {
            throw new RuntimeException(Translator.toLocale("ex.less.value"));
        }
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        long id = giftCertificateDao.insert(giftCertificate).longValue();
        if (tags != null) {
            tags.forEach(t -> {
                if (tagDao.findByName(t.getName()).isEmpty()) {
                    tagDao.insert(t);
                }
                giftCertificateDao.addTagToGiftCertificate(id, tagDao.findByName(t.getName()).get().getId());
            });
        }
        return find(id);
    }

    @Override
    @Transactional
    public void update(GiftCertificateDto giftCertificateDto, long id) {
        Optional<GiftCertificate> optGift = giftCertificateDao.find(id);
        GiftCertificate giftCertificate = new GiftCertificate(giftCertificateDto);
        if (!String.valueOf(giftCertificate.getPrice()).matches(DOUBLE_REGEX)
                || !String.valueOf(giftCertificate.getDuration()).matches(INTEGER_REGEX)) {
            throw new RuntimeException(Translator.toLocale("ex.less.value"));
        }
        giftCertificate.setId(id);
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        giftCertificate.setCreateDate(optGift.get().getCreateDate());
        List<Tag> tags = giftCertificateDto.getTags();
        giftCertificateDao.update(giftCertificate);
        giftCertificateDao.removeTagToGiftCertificate(id);
        tags.forEach(t -> {
            if (tagDao.findByName(t.getName()).isEmpty()) {
                tagDao.insert(t);
            }
            giftCertificateDao.addTagToGiftCertificate(id, tagDao.findByName(t.getName()).get().getId());
        });
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> descSortByDate() {
        List<GiftCertificateDto> dtoList = new ArrayList<>();
        List<GiftCertificate> giftCertificates = giftCertificateDao.descSortByDate();
        giftCertificates.forEach(g -> {
            GiftCertificateDto giftCertificateDto = new GiftCertificateDto(g);
            List<Tag> allTagsByCertificateId = tagDao.findAllTagsByCertificateId(g.getId());
            giftCertificateDto.setTags(allTagsByCertificateId);
            dtoList.add(giftCertificateDto);
        });
        return dtoList;
    }
}

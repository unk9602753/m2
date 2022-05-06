package com.epam.esm.service.impl;

import com.epam.esm.config.Translator;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.NumberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    @Autowired
    private final NumberValidator numberValidator;

    @Override
    @Transactional
    public void delete(long id) {
        Optional<GiftCertificateDto> optCertificate = find(id);
        if (optCertificate.isPresent()) {
            giftCertificateDao.remove(id);
        }
    }

    @Override
    @Transactional
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
    public List<GiftCertificateDto> sort(String direction, String criteria) {
        return null;
    }

    @Override
    @Transactional
    public Optional<GiftCertificateDto> create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = new GiftCertificate(giftCertificateDto);
        List<Tag> tags = giftCertificateDto.getTags();
        if (!isValidPriceAndDuration(giftCertificate)) {
            throw new RuntimeException(Translator.toLocale("ex.less.value"));
        }
        setCreatedAndUpdatedDate(giftCertificate,LocalDateTime.now(),LocalDateTime.now());
        long id = giftCertificateDao.insert(giftCertificate).longValue();
        if (tags != null) {
            tags.stream().filter(t->tagDao.findByName(t.getName()).isEmpty()).forEach(System.out::println);
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
        if (!isValidPriceAndDuration(giftCertificate)) {
            throw new RuntimeException(Translator.toLocale("ex.less.value"));
        }
        setCreatedAndUpdatedDate(giftCertificate,optGift.get().getCreateDate(),LocalDateTime.now());
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

    private boolean isValidPriceAndDuration(GiftCertificate giftCertificate){
        return numberValidator.isPositiveDouble(String.valueOf(giftCertificate.getPrice()))
                && numberValidator.isPositiveInteger(String.valueOf(giftCertificate.getDuration()));
    }

    private void setCreatedAndUpdatedDate(GiftCertificate giftCertificate, LocalDateTime created, LocalDateTime updated){
        giftCertificate.setCreateDate(created);
        giftCertificate.setLastUpdateDate(updated);
    }
}
//    @Override
//    @Transactional
//    public List<GiftCertificateDto> ascSortByDate() {
//        List<GiftCertificateDto> dtoList = new ArrayList<>();
//        List<GiftCertificate> giftCertificates = giftCertificateDao.ascSortByDate();
//        giftCertificates.forEach(g -> {
//            GiftCertificateDto giftCertificateDto = new GiftCertificateDto(g);
//            List<Tag> allTagsByCertificateId = tagDao.findAllTagsByCertificateId(g.getId());
//            giftCertificateDto.setTags(allTagsByCertificateId);
//            dtoList.add(giftCertificateDto);
//        });
//        return dtoList;
//    }
//
//    @Override
//    @Transactional
//    public List<GiftCertificateDto> descSortByDate() {
//        List<GiftCertificateDto> dtoList = new ArrayList<>();
//        List<GiftCertificate> giftCertificates = giftCertificateDao.descSortByDate();
//        giftCertificates.forEach(g -> {
//            GiftCertificateDto giftCertificateDto = new GiftCertificateDto(g);
//            List<Tag> allTagsByCertificateId = tagDao.findAllTagsByCertificateId(g.getId());
//            giftCertificateDto.setTags(allTagsByCertificateId);
//            dtoList.add(giftCertificateDto);
//        });
//        return dtoList;
//    }
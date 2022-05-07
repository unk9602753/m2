package com.epam.esm.service.impl;

import com.epam.esm.config.Translator;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.math.NumberUtils;
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

    @Override
    @SneakyThrows(ServiceException.class)
    public void delete(long id) {
        int statement = giftCertificateDao.remove(id);
        if(statement == 0){
            throw new ServiceException(Translator.toLocale("exception.remove.certificate"));
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
        List<GiftCertificate> giftCertificates = giftCertificateDao.findAll();
        return dtoPresenting(giftCertificates);
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> findByCriteria(String criteria, String name) {
        List<GiftCertificate> giftCertificates = giftCertificateDao.findByCriteria(criteria, name);
        return dtoPresenting(giftCertificates);
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> sort(String direction, String criteria) {
        List<GiftCertificate> giftCertificates = giftCertificateDao.sort(direction, criteria);
        return dtoPresenting(giftCertificates);
    }

    @Override
    @Transactional
    @SneakyThrows(ServiceException.class)
    public void create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = new GiftCertificate(giftCertificateDto);
        List<Tag> tags = giftCertificateDto.getTags();
        if (isValidPriceAndDuration(giftCertificate)) {
            setCreatedAndUpdatedDate(giftCertificate, LocalDateTime.now(), LocalDateTime.now());
            long id = giftCertificateDao.insert(giftCertificate).longValue();
            linkCertificateWithTags(tags, id);
        } else {
            throw new ServiceException(Translator.toLocale("exception.create.certificate"));
        }
    }

    @Override
    @Transactional
    @SneakyThrows(ServiceException.class)
    public void update(GiftCertificateDto giftCertificateDto, long id) {
        Optional<GiftCertificate> optGift = giftCertificateDao.find(id);
        GiftCertificate giftCertificate = new GiftCertificate(giftCertificateDto);
        if (isValidPriceAndDuration(giftCertificate)) {
            setCreatedAndUpdatedDate(giftCertificate, optGift.get().getCreateDate(), LocalDateTime.now());
            List<Tag> tags = giftCertificateDto.getTags();
            giftCertificateDao.update(giftCertificate);
            giftCertificateDao.removeTagToGiftCertificate(id);
            linkCertificateWithTags(tags, id);
        } else {
            throw new ServiceException(Translator.toLocale("exception.update.certificate"));
        }
    }

    @Transactional
    void linkCertificateWithTags(List<Tag> tags, long certificateId) {
        if (tags != null) {
            tags.forEach(t -> {
                if (tagDao.findByName(t.getName()).isEmpty()) {
                    tagDao.insert(t);
                }
                giftCertificateDao.addTagToGiftCertificate(certificateId, tagDao.findByName(t.getName()).get().getId());
            });
        }
    }

    private List<GiftCertificateDto> dtoPresenting(List<GiftCertificate> giftCertificates) {//todo --name of method--
        List<GiftCertificateDto> dtoList = new ArrayList<>();
        giftCertificates.forEach(g -> {
            GiftCertificateDto giftCertificateDto = new GiftCertificateDto(g);
            List<Tag> allTagsByCertificateId = tagDao.findAllTagsByCertificateId(g.getId());
            giftCertificateDto.setTags(allTagsByCertificateId);
            dtoList.add(giftCertificateDto);
        });
        return dtoList;
    }

    private boolean isValidPriceAndDuration(GiftCertificate giftCertificate) {
        return NumberUtils.compare((int) (double) giftCertificate.getPrice(), 0) > 0
                && NumberUtils.compare(giftCertificate.getDuration(), 0) > 0;
    }

    @Override
    public void updateObject(GiftCertificateDto dto, GiftCertificateDto patch) {//todo use reflection?
        if (patch.getName() != null) {
            dto.setName(patch.getName());
        }
        if (patch.getDescription() != null) {
            dto.setDescription(patch.getDescription());
        }
        if (patch.getTags() != null) {
            dto.setTags(patch.getTags());
        }
        if (patch.getPrice() != null) {
            dto.setPrice(patch.getPrice());
        }
        if (patch.getDuration() != null) {
            dto.setDuration(patch.getDuration());
        }
    }

    private void setCreatedAndUpdatedDate(GiftCertificate giftCertificate, LocalDateTime created, LocalDateTime updated) {
        giftCertificate.setCreateDate(created);
        giftCertificate.setLastUpdateDate(updated);
    }
}

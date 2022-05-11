package com.epam.esm.service.impl;

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
        if (statement == 0) {
            throw new ServiceException("exception.delete.certificate",id);
        }
    }

    @Override
    @Transactional
    @SneakyThrows(ServiceException.class)
    public Optional<GiftCertificateDto> find(long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateDao.find(id);
        List<Tag> allTagsByCertificateId = tagDao.findAllTagsByCertificateId(id);
        if (giftCertificate.isPresent()) {
            GiftCertificateDto giftCertificateDto = new GiftCertificateDto(giftCertificate.get());
            giftCertificateDto.setTags(allTagsByCertificateId);
            return Optional.of(giftCertificateDto);
        } else {
            throw new ServiceException("exception.find.certificate", id);
        }
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> findAll() {
        throw new UnsupportedOperationException("Unsupported method");
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> findByCriteria(String criteria, String name) {
        List<GiftCertificate> giftCertificates = giftCertificateDao.findByCriteria(criteria, name);
        return listOfDtos(giftCertificates);
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> findAllByCriteria(String direction, String criteria) {
        List<GiftCertificate> giftCertificates = direction == null || criteria == null
                ? giftCertificateDao.findAll()
                : giftCertificateDao.sort(direction, criteria);
        return listOfDtos(giftCertificates);
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
            throw new ServiceException("exception.create.certificate");
        }
    }

    @Override
    @Transactional
    @SneakyThrows(ServiceException.class)
    public void update(GiftCertificateDto patch, long id) {
        Optional<GiftCertificate> optGift = giftCertificateDao.find(id);
        GiftCertificate certificate = new GiftCertificate(patch);
        GiftCertificate updatedCertificate = updateObject(optGift.get(), certificate);
        if (isValidPriceAndDuration(updatedCertificate)) {
            setCreatedAndUpdatedDate(updatedCertificate, optGift.get().getCreateDate(), LocalDateTime.now());
            List<Tag> tags = patch.getTags();
            giftCertificateDao.update(updatedCertificate);
            giftCertificateDao.removeTagToGiftCertificate(id);
            linkCertificateWithTags(tags, id);
        } else {
            throw new ServiceException("exception.update.certificate",id);
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

    private List<GiftCertificateDto> listOfDtos(List<GiftCertificate> giftCertificates) {
        List<GiftCertificateDto> dtoList = new ArrayList<>();
        if(giftCertificates != null){
            giftCertificates.forEach(g -> {
                GiftCertificateDto giftCertificateDto = new GiftCertificateDto(g);
                List<Tag> allTagsByCertificateId = tagDao.findAllTagsByCertificateId(g.getId());
                giftCertificateDto.setTags(allTagsByCertificateId);
                dtoList.add(giftCertificateDto);
            });
        }
        return dtoList;
    }

    private boolean isValidPriceAndDuration(GiftCertificate giftCertificate) {
        return NumberUtils.compare((int) giftCertificate.getPrice(), 0) > 0
                && NumberUtils.compare(giftCertificate.getDuration(), 0) > 0;
    }

    private GiftCertificate updateObject(GiftCertificate certificate, GiftCertificate patch) {
        if (patch.getName() != null) {
            certificate.setName(patch.getName());
        }
        if (patch.getDescription() != null) {
            certificate.setDescription(patch.getDescription());
        }
        if (patch.getPrice() != 0) {
            certificate.setPrice(patch.getPrice());
        }
        if (patch.getDuration() != 0) {
            certificate.setDuration(patch.getDuration());
        }
        return certificate;
    }

    private void setCreatedAndUpdatedDate(GiftCertificate giftCertificate, LocalDateTime created, LocalDateTime updated) {
        giftCertificate.setCreateDate(created);
        giftCertificate.setLastUpdateDate(updated);
    }
}

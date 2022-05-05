package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    @Override
    @Transactional
    public Optional<Tag> find(long id) {
        return tagDao.find(id);
    }

    @Override
    @Transactional
    public List<Tag> findAll() {
        return tagDao.findAll();
    }

    @Override
    @Transactional
    public Optional<Tag> delete(long id) {
        Optional<Tag> optTag = tagDao.find(id);
        if (optTag.isPresent()) {
            tagDao.remove(id);
            return optTag;
        }
        return optTag;
    }

    @Override
    @Transactional
    public Optional<Tag> create(Tag tag) {
        if (tag != null && tagDao.findByName(tag.getName()).isEmpty()) {
            long id = tagDao.insert(tag).longValue();
            tag.setId(id);
            return Optional.of(tag);
        }
        return Optional.empty();
    }
}

package com.epam.esm.service.impl;

import com.epam.esm.config.Translator;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    @Override
    public Optional<Tag> find(long id) {
        return tagDao.find(id);
    }

    @Override
    public List<Tag> findAll() {
        return tagDao.findAll();
    }

    @Override
    @SneakyThrows(ServiceException.class)
    public void delete(long id) {
        int statement = tagDao.remove(id);
        if(statement == 0){
            throw new ServiceException(Translator.toLocale("exception.remove.tag"));
        }
    }

    @Override
    @Transactional
    @SneakyThrows(ServiceException.class)
    public void create(Tag tag) {
        boolean isTagExist = tagDao.findByName(tag.getName()).isPresent();
        if (!isTagExist) {
            tagDao.insert(tag);
        }else{
            throw new ServiceException("exception.tag.exist");
        }
    }
}


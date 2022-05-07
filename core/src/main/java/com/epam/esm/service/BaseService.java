package com.epam.esm.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {
    Optional<T> find(long id);

    List<T> findAll();

    void create(T entity);

    void delete(long id);
}

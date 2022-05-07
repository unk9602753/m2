package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Number insert(T entity);

    void update(T entity);

    Optional<T> find(long id);

    List<T> findAll();

    int remove(long id);
}

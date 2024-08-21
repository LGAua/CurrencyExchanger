package com.lga.dao;

import java.util.List;
import java.util.Optional;

public interface Dao <K,T>{
    Optional<T> findById(K id);

    List<T> findAll();

    Optional<T> save(T entity);


    T update(T entity);
}

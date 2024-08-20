package com.lga.dao;

import java.util.List;

public interface Dao <K,T>{
    T findById(K id);

    List<T> findAll();

    T save(T entity);

    T update(T entity);
}

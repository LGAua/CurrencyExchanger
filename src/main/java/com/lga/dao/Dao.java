package com.lga.dao;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;

public interface Dao <K,T>{
    Optional<T> findById(K id);

    List<T> findAll();

    Optional<T> save(T entity);
=======

public interface Dao <K,T>{
    T findById(K id);

    List<T> findAll();

    T save(T entity);
>>>>>>> 4ef3db2876c01d75d24bee59b73559d26ce8da34

    T update(T entity);
}

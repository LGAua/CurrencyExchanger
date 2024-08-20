package com.lga.mapper;

public interface Mapper<F,T>{
    T mapFrom(F object);
}

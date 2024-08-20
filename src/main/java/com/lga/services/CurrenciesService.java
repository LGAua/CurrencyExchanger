package com.lga.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lga.dao.CurrenciesDao;
import com.lga.dto.CurrencyForSaveDto;
import com.lga.entity.CurrencyEntity;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CurrenciesService {
    private static final CurrenciesService INSTANCE = new CurrenciesService();

    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    public static CurrenciesService getInstance() {
        return INSTANCE;
    }

    public List<String> findAll() {
        ObjectMapper objectMapper = new ObjectMapper();
        return currenciesDao.findAll().stream()
                .map(entity -> {
                    try {
                        return objectMapper.writeValueAsString(entity);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(toList());
    }

    public String save(CurrencyForSaveDto currencyForSaveDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyEntity currencyEntity = currenciesDao.save(CurrencyEntity.builder()
                .code(currencyForSaveDto.getCode())
                .fullName(currencyForSaveDto.getFullName())
                .sign(currencyForSaveDto.getSign())
                .build());
        try {
            return  objectMapper.writeValueAsString(currencyEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

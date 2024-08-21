package com.lga.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lga.dao.CurrenciesDao;
import com.lga.dto.CurrencyForSaveDto;
import com.lga.entity.CurrencyEntity;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;


import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CurrenciesService {
    private static final CurrenciesService INSTANCE = new CurrenciesService();
    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static CurrenciesService getInstance() {
        return INSTANCE;
    }

    public List<String> findAll() {
        return currenciesDao.findAll().stream()
                .map(this::convertToJson)
                .collect(toList());
    }

    public Optional<String> save(CurrencyForSaveDto currencyForSaveDto) {
        Optional<CurrencyEntity> currencyEntity = currenciesDao.save(CurrencyEntity.builder()
                .code(currencyForSaveDto.getCode())
                .fullName(currencyForSaveDto.getFullName())
                .sign(currencyForSaveDto.getSign())
                .build());
        return currencyEntity.map(this::convertToJson);
    }

    @SneakyThrows
    private String convertToJson(CurrencyEntity currencyEntity) {
        return objectMapper.writeValueAsString(currencyEntity);
    }
}


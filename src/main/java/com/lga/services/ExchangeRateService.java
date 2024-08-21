package com.lga.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lga.dao.ExchangeRateDao;
import com.lga.dto.ExchangeRateForSaveDto;
import com.lga.entity.ExchangeRateEntity;
import com.lga.mapper.ExchangeRateDtoMapper;
import com.lga.mapper.ExchangeRateEntityMapper;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();

    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final ExchangeRateDtoMapper exchangerRateDtoMapper = ExchangeRateDtoMapper.getInstance();
    private final ExchangeRateEntityMapper exchangeRateEntityMapper = ExchangeRateEntityMapper.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }

    public List<String> findAll() {
        return exchangeRateDao.findAll().stream()
                .map(this::convertToJson)
                .collect(toList());
    }

    public Optional<String> save(ExchangeRateForSaveDto exchangeRateForSaveDto) {
        ExchangeRateEntity exchangeRateEntity = exchangeRateEntityMapper.mapFrom(exchangeRateForSaveDto);
        Optional<ExchangeRateEntity> savedExchangeRateEntity = exchangeRateDao.save(exchangeRateEntity);
        return savedExchangeRateEntity.map(this::convertToJson);
    }

    @SneakyThrows
    private String convertToJson(ExchangeRateEntity entity) {
            return objectMapper.writeValueAsString(exchangerRateDtoMapper.mapFrom(entity));
    }

}

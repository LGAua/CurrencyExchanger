package com.lga.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lga.dao.ExchangeRateDao;
import com.lga.dto.ExchangeRateForSaveDto;
import com.lga.entity.ExchangeRateEntity;
import com.lga.mapper.ExchangeRateDtoMapper;
import com.lga.mapper.ExchangeRateEntityMapper;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();

    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final ExchangeRateDtoMapper exchangerRateDtoMapper = ExchangeRateDtoMapper.getInstance();
    private final ExchangeRateEntityMapper exchangeRateEntityMapper = ExchangeRateEntityMapper.getInstance();

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }

    public List<String> findAll() {
        return exchangeRateDao.findAll().stream()
                .map(entity -> convertToJson(entity))
                .collect(toList());
    }

    public String save(ExchangeRateForSaveDto exchangeRateForSaveDto) {
        ExchangeRateEntity exchangeRateEntity = exchangeRateEntityMapper.mapFrom(exchangeRateForSaveDto);
        ExchangeRateEntity exchangeRateEntity2 = exchangeRateDao.save(exchangeRateEntity);
        String s = convertToJson(exchangeRateEntity2);
        return s;
    }

    private String convertToJson(ExchangeRateEntity entity) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(exchangerRateDtoMapper.mapFrom(entity));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

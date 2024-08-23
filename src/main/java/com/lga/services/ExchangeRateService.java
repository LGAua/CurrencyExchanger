package com.lga.services;


import com.lga.dto.ExchangeRateDto;
import com.lga.dto.ExchangeRateForSaveDto;
import com.lga.entity.CurrencyEntity;
import com.lga.entity.ExchangeRateEntity;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.lga.util.SingletonConstants.DaoConstants.currenciesDao;
import static com.lga.util.SingletonConstants.DaoConstants.exchangeRateDao;
import static com.lga.util.SingletonConstants.MapperConstants.exchangeRateEntityMapper;
import static com.lga.util.SingletonConstants.MapperConstants.exchangerRateDtoMapper;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }

    public List<ExchangeRateDto> findAll() {
        return exchangeRateDao.findAll().stream()
                .map(exchangerRateDtoMapper::mapFrom)
                .collect(toList());
    }

    public Optional<ExchangeRateDto> findExchangeRateByCurrencyCode(String baseCurrencyCode, String targetCurrencyCode) {
        Integer baseCurrencyId = findCurrencyIdByCode(baseCurrencyCode);
        Integer targetCurrencyId = findCurrencyIdByCode(targetCurrencyCode);
        Optional<ExchangeRateEntity> exchangeRateEntity = exchangeRateDao.findByBaseIdAndTargetId(baseCurrencyId, targetCurrencyId);
        return exchangeRateEntity.map(exchangerRateDtoMapper::mapFrom);
    }

    public Optional<ExchangeRateDto> save(ExchangeRateForSaveDto exchangeRateForSaveDto) {
        ExchangeRateEntity exchangeRateEntity = exchangeRateEntityMapper.mapFrom(exchangeRateForSaveDto);
        Optional<ExchangeRateEntity> savedExchangeRateEntity = exchangeRateDao.save(exchangeRateEntity);
        return savedExchangeRateEntity.map(exchangerRateDtoMapper::mapFrom);
    }

    public Optional<ExchangeRateDto> update(ExchangeRateForSaveDto exchangeRateForSaveDto) {
        ExchangeRateEntity exchangeRateEntity = exchangeRateEntityMapper.mapFrom(exchangeRateForSaveDto);
        ExchangeRateEntity updateResult = exchangeRateDao.update(exchangeRateEntity);
        Optional<ExchangeRateEntity> updatedExchangeRateEntity = Optional.ofNullable(updateResult);
        return updatedExchangeRateEntity.map(exchangerRateDtoMapper::mapFrom);
    }

    private Integer findCurrencyIdByCode(String baseCurrencyCode) {
        Optional<CurrencyEntity> currencyEntity = currenciesDao.findByCode(baseCurrencyCode);
        return currencyEntity.orElseThrow().getId();
    }
}

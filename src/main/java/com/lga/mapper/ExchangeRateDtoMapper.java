package com.lga.mapper;

import com.lga.dao.CurrenciesDao;
import com.lga.dto.ExchangeRateDto;
import com.lga.entity.ExchangeRateEntity;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ExchangeRateDtoMapper implements Mapper<ExchangeRateEntity, ExchangeRateDto> {
    private static final ExchangeRateDtoMapper INSTANCE = new ExchangeRateDtoMapper();
    private static final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    public static ExchangeRateDtoMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeRateDto mapFrom(ExchangeRateEntity object) {
        return ExchangeRateDto.builder()
                .id(object.getId())
                .baseCurrencyEntity(currenciesDao.findById(object.getBaseCurrencyId()))
                .targetCurrencyEntity(currenciesDao.findById(object.getTargetCurrencyId()))
                .rate(object.getRate())
                .build();
    }
}

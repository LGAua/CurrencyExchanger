package com.lga.mapper;

import com.lga.dao.CurrenciesDao;
import com.lga.dto.ExchangeRateDto;
import com.lga.entity.CurrencyEntity;

import com.lga.entity.ExchangeRateEntity;

import lombok.NoArgsConstructor;

import java.util.Optional;

import static com.lga.util.Constants.DaoConstants.currenciesDao;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ExchangeRateDtoMapper implements Mapper<ExchangeRateEntity, ExchangeRateDto> {
    private static final ExchangeRateDtoMapper INSTANCE = new ExchangeRateDtoMapper();

    public static ExchangeRateDtoMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeRateDto mapFrom(ExchangeRateEntity object) {
        return ExchangeRateDto.builder()
                .id(object.getId())
                .baseCurrencyEntity(findCurrencyById(object.getBaseCurrencyId()))
                .targetCurrencyEntity(findCurrencyById(object.getTargetCurrencyId()))
                .rate(object.getRate())
                .build();
    }

    private CurrencyEntity findCurrencyById(Integer id) {
        Optional<CurrencyEntity> currencyEntity = currenciesDao.findById(id);
        return currencyEntity.orElse(null);
    }

}

package com.lga.mapper;

import com.lga.dao.CurrenciesDao;
import com.lga.dto.ExchangeRateForSaveDto;
import com.lga.entity.ExchangeRateEntity;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ExchangeRateEntityMapper implements Mapper<ExchangeRateForSaveDto, ExchangeRateEntity>{
    private static final ExchangeRateEntityMapper INSTANCE = new ExchangeRateEntityMapper();
    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    public static ExchangeRateEntityMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeRateEntity mapFrom(ExchangeRateForSaveDto object) {
        return ExchangeRateEntity.builder()
                .baseCurrencyId(findCurrencyIdByCode(object.getBaseCurrencyCode()))
                .targetCurrencyId(findCurrencyIdByCode(object.getTargetCurrencyCode()))
                .rate(new BigDecimal(object.getRate()))
                .build();
    }

    private Integer findCurrencyIdByCode(String code){
        return currenciesDao.findByCode(code).getId();
    }
}

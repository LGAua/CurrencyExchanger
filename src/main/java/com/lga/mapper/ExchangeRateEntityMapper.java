package com.lga.mapper;

import com.lga.dao.CurrenciesDao;
import com.lga.dto.ExchangeRateForSaveDto;
import com.lga.entity.CurrencyEntity;
import com.lga.entity.ExchangeRateEntity;
import com.lga.exceptions.CurrencyNotFoundException;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

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

    //Optional???
    private Integer findCurrencyIdByCode(String code) throws CurrencyNotFoundException{
        Optional<CurrencyEntity> currencyEntity = currenciesDao.findByCode(code);
        if(currencyEntity.isEmpty()){
            throw new CurrencyNotFoundException();
        }
        return currenciesDao.findByCode(code).get().getId();
    }
}

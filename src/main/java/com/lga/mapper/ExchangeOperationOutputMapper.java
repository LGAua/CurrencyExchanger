package com.lga.mapper;

import com.lga.dao.CurrenciesDao;
import com.lga.dao.ExchangeRateDao;
import com.lga.dto.ExchangeOperationInputDto;
import com.lga.dto.ExchangeOperationOutputDto;
import com.lga.entity.CurrencyEntity;
import com.lga.entity.ExchangeRateEntity;
import com.lga.exceptions.CurrencyNotFoundException;
import com.lga.exceptions.ExchangeRatePairNotFoundException;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ExchangeOperationOutputMapper implements Mapper<ExchangeOperationInputDto, ExchangeOperationOutputDto> {
    private static final ExchangeOperationOutputMapper INSTANCE = new ExchangeOperationOutputMapper();

    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();

    public static ExchangeOperationOutputMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeOperationOutputDto mapFrom(ExchangeOperationInputDto object) {
        ExchangeOperationOutputDto outputDto = ExchangeOperationOutputDto.builder()
                .baseCurrencyEntity(findCurrencyEntityByCode(object.getBaseCurrencyCode()))
                .targetCurrencyEntity(findCurrencyEntityByCode(object.getTargetCurrencyCode()))
                .rate(findExchangeRate(object.getBaseCurrencyCode(), object.getTargetCurrencyCode()))
                .amount(Integer.parseInt(object.getAmount()))
                .convertedAmount(calculateAmount(object))
                .build();

        return outputDto;
    }

    private CurrencyEntity findCurrencyEntityByCode(String code) throws CurrencyNotFoundException {
        Optional<CurrencyEntity> currencyEntity = currenciesDao.findByCode(code);
        if (currencyEntity.isEmpty()) {
            throw new CurrencyNotFoundException();
        }
        return currencyEntity.get();
    }

    private BigDecimal findExchangeRate(String baseCurrencyCode, String targetCurrencyCode) throws ExchangeRatePairNotFoundException {
        CurrencyEntity base = currenciesDao.findByCode(baseCurrencyCode).get();
        CurrencyEntity target = currenciesDao.findByCode(targetCurrencyCode).get();
        Optional<ExchangeRateEntity> exchangeRateEntity = exchangeRateDao.findByBaseIdAndTargetId(base.getId(), target.getId());
        if (exchangeRateEntity.isEmpty()){
            throw new ExchangeRatePairNotFoundException();
        }
        return exchangeRateEntity.get().getRate();
    }

    private BigDecimal calculateAmount(ExchangeOperationInputDto object) {
        CurrencyEntity base = currenciesDao.findByCode(object.getBaseCurrencyCode()).get();
        CurrencyEntity target = currenciesDao.findByCode(object.getTargetCurrencyCode()).get();
        BigDecimal rate = exchangeRateDao.findByBaseIdAndTargetId(base.getId(), target.getId()).get().getRate();
        BigDecimal amount = new BigDecimal(Integer.parseInt(object.getAmount()));
        return rate.multiply(amount);
    }
}

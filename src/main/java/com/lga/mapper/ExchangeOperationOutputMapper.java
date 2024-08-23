package com.lga.mapper;

import com.lga.dto.ExchangeOperationIntermediateDto;
import com.lga.dto.ExchangeOperationOutputDto;
import com.lga.entity.CurrencyEntity;
import com.lga.exceptions.CurrencyNotFoundException;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static com.lga.util.SingletonConstants.DaoConstants.currenciesDao;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ExchangeOperationOutputMapper implements Mapper<ExchangeOperationIntermediateDto, ExchangeOperationOutputDto> {

    private static final ExchangeOperationOutputMapper INSTANCE = new ExchangeOperationOutputMapper();

    public static ExchangeOperationOutputMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeOperationOutputDto mapFrom(ExchangeOperationIntermediateDto object) {
        return ExchangeOperationOutputDto.builder()
                .baseCurrencyEntity(findCurrencyEntityById(object.getBaseCurrencyId()))
                .targetCurrencyEntity(findCurrencyEntityById(object.getTargetCurrencyId()))
                .rate(object.getRate())
                .amount(object.getAmount())
                .convertedAmount(object.getConvertedAmount())
                .build();
    }

    private CurrencyEntity findCurrencyEntityById(Integer currencyId) throws CurrencyNotFoundException {
        Optional<CurrencyEntity> currencyEntity = currenciesDao.findById(currencyId);
        if (currencyEntity.isEmpty()) {
            throw new CurrencyNotFoundException();
        }
        return currencyEntity.get();
    }
}

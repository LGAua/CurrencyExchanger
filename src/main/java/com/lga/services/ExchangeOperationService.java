package com.lga.services;

import com.lga.dto.ExchangeOperationInputDto;
import com.lga.dto.ExchangeOperationOutputDto;
import lombok.NoArgsConstructor;

import static com.lga.util.SingletonConstants.MapperConstants.exchangeOperationOutputMapper;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ExchangeOperationService {
    private static final ExchangeOperationService INSTANCE = new ExchangeOperationService();


    public static ExchangeOperationService getInstance() {
        return INSTANCE;
    }

    public ExchangeOperationOutputDto makeCurrencyExchange(ExchangeOperationInputDto exchangeOperationInputDto) {
        return exchangeOperationOutputMapper.mapFrom(exchangeOperationInputDto);
    }
}

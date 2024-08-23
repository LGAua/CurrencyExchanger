package com.lga.services;

import com.lga.dto.ExchangeOperationInputDto;
import com.lga.dto.ExchangeOperationOutputDto;
import com.lga.mapper.ExchangeOperationOutputMapper;
import lombok.NoArgsConstructor;

import static com.lga.util.Constants.MapperConstants.exchangeOperationOutputMapper;
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

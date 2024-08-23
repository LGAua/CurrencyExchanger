package com.lga.services;

import com.lga.dto.ExchangeOperationInputDto;
import com.lga.dto.ExchangeOperationOutputDto;
import com.lga.mapper.ExchangeOperationOutputMapper;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ExchangeOperationService {
    private static final ExchangeOperationService INSTANCE = new ExchangeOperationService();

    private final ExchangeOperationOutputMapper exchangeOperationOutputMapper = ExchangeOperationOutputMapper.getInstance();

    public static ExchangeOperationService getInstance() {
        return INSTANCE;
    }

    public ExchangeOperationOutputDto makeCurrencyExchange(ExchangeOperationInputDto exchangeOperationInputDto) {
        return exchangeOperationOutputMapper.mapFrom(exchangeOperationInputDto);
    }
}

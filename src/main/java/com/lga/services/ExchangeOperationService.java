package com.lga.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lga.dto.ExchangeOperationInputDto;
import com.lga.dto.ExchangeOperationOutputDto;
import com.lga.mapper.ExchangeOperationOutputMapper;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ExchangeOperationService {
    private static final ExchangeOperationService INSTANCE = new ExchangeOperationService();

    private final ExchangeOperationOutputMapper exchangeOperationOutputMapper = ExchangeOperationOutputMapper.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static ExchangeOperationService getInstance() {
        return INSTANCE;
    }

    public String makeCurrencyExchange(ExchangeOperationInputDto exchangeOperationInputDto) {
        ExchangeOperationOutputDto outputDto = exchangeOperationOutputMapper.mapFrom(exchangeOperationInputDto);
        return convertToJson(outputDto);
    }

    public BigDecimal calculateConvertedAmount(BigDecimal rate, Integer amount){
        BigDecimal bigDecimalAmount = new BigDecimal(amount);
        BigDecimal multiplied = rate.multiply(bigDecimalAmount);
        return multiplied;

    }

    @SneakyThrows
    private String convertToJson(ExchangeOperationOutputDto outputDto) {
        return objectMapper.writeValueAsString(outputDto);
    }
}

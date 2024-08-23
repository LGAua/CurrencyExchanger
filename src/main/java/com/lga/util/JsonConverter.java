package com.lga.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lga.dto.ExchangeOperationOutputDto;
import com.lga.dto.ExchangeRateDto;
import com.lga.entity.CurrencyEntity;
import com.lga.mapper.ExchangeRateDtoMapper;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class JsonConverter {
    private static final JsonConverter INSTANCE = new JsonConverter();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExchangeRateDtoMapper exchangerRateDtoMapper = ExchangeRateDtoMapper.getInstance();

    public static JsonConverter getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public String convertToJson(CurrencyEntity currencyEntity) {
        return objectMapper.writeValueAsString(currencyEntity);
    }

    @SneakyThrows
    public String convertToJson(ExchangeOperationOutputDto outputDto) {
        return objectMapper.writeValueAsString(outputDto);
    }

    @SneakyThrows
    public String convertToJson(ExchangeRateDto exchangeRateDto) {
        return objectMapper.writeValueAsString(exchangeRateDto);
    }


}

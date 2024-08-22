package com.lga.dto;

import com.lga.entity.CurrencyEntity;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangeOperationOutputDto {
    CurrencyEntity baseCurrencyEntity;
    CurrencyEntity targetCurrencyEntity;
    BigDecimal rate;
    Integer amount;
    BigDecimal convertedAmount;
}

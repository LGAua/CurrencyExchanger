package com.lga.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangeOperationIntermediateDto {
    Integer baseCurrencyId;
    Integer targetCurrencyId;
    BigDecimal rate;
    Integer amount;
    BigDecimal convertedAmount;
}

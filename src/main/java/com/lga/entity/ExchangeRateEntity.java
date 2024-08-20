package com.lga.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class ExchangeRateEntity {
    Integer id;
    Integer baseCurrencyId;
    Integer targetCurrencyId;
    BigDecimal rate;
}

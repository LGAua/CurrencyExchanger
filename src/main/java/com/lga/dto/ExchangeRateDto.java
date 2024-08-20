package com.lga.dto;

import com.lga.entity.CurrencyEntity;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;

@Value
@Builder
public class ExchangeRateDto {
    Integer id;
    CurrencyEntity baseCurrencyEntity;
    CurrencyEntity targetCurrencyEntity;
    BigDecimal rate;
}

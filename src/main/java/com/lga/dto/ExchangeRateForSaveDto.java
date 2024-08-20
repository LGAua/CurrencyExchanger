package com.lga.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExchangeRateForSaveDto {
    String baseCurrencyCode;
    String targetCurrencyCode;
    String rate;
}

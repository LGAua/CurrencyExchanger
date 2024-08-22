package com.lga.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExchangeOperationInputDto {
    String baseCurrencyCode;
    String targetCurrencyCode;
    String amount;
}

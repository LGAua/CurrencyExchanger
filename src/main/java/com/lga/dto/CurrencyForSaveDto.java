package com.lga.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CurrencyForSaveDto {
    String code;
    String fullName;
    String sign;
}

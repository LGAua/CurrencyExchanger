package com.lga.entity;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class CurrencyEntity {
    Integer id;
    String code;
    String fullName;
    String sign;
}

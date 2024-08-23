package com.lga.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpResponseTextConstants {
    public static final String FIELD_IS_EMPTY = "Bad request. One of required fields is empty";
    public static final String CURRENCY_ALREADY_EXISTS = "Conflict. Currency with such code already exists";
    public static final String EXCHANGE_RATE_ALREADY_EXISTS = "Conflict. Exchange rate already exists";
    public static final String CURRENCY_NOT_FOUND = "Not found. Currency code does not exist";
    public static final String CURRENCY_PAIR_NOT_FOUND = "Not found. Currency pair does not exist";
    public static final String REQUEST_FIELDS_INVALID = "Bad request. Currency pair is invalid";
    public static final String CURRENCY_PAIR_INVALID = "Bad request. Currency pair is invalid";
    public static final String CURRENCY_RATE_ABSENT = "Bad request. Currency rate is absent";
}

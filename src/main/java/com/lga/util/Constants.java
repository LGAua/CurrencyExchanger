package com.lga.util;

import com.lga.dao.CurrenciesDao;
import com.lga.dao.ExchangeRateDao;
import com.lga.mapper.ExchangeOperationOutputMapper;
import com.lga.mapper.ExchangeRateDtoMapper;
import com.lga.mapper.ExchangeRateEntityMapper;
import com.lga.services.CurrenciesService;
import com.lga.services.ExchangeOperationService;
import com.lga.services.ExchangeRateService;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static class DaoConstants {
        public static final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
        public static final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();
    }

    public static class MapperConstants {
        public static final ExchangeRateDtoMapper exchangerRateDtoMapper = ExchangeRateDtoMapper.getInstance();
        public static final ExchangeRateEntityMapper exchangeRateEntityMapper = ExchangeRateEntityMapper.getInstance();
        public static final ExchangeOperationOutputMapper exchangeOperationOutputMapper = ExchangeOperationOutputMapper.getInstance();
    }

    public static class ServiceConstants {
        public static final CurrenciesService currenciesService = CurrenciesService.getInstance();
        public static final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
        public static final ExchangeOperationService exchangeOperationService = ExchangeOperationService.getInstance();

    }

    public static class UtilConstant {
        public static final JsonConverter jsonConverter = JsonConverter.getInstance();
    }
}

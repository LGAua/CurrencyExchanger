package com.lga.services;

import com.lga.dto.ExchangeOperationInputDto;
import com.lga.dto.ExchangeOperationIntermediateDto;
import com.lga.dto.ExchangeOperationOutputDto;
import com.lga.entity.CurrencyEntity;
import com.lga.entity.ExchangeRateEntity;
import com.lga.exceptions.CurrencyNotFoundException;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

import static com.lga.util.SingletonConstants.DaoConstants.currenciesDao;
import static com.lga.util.SingletonConstants.DaoConstants.exchangeRateDao;
import static com.lga.util.SingletonConstants.MapperConstants.exchangeOperationOutputMapper;
import static java.math.MathContext.DECIMAL64;
import static java.math.RoundingMode.HALF_UP;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ExchangeOperationService {
    private static final ExchangeOperationService INSTANCE = new ExchangeOperationService();

    public static ExchangeOperationService getInstance() {
        return INSTANCE;
    }

    public Optional<ExchangeOperationOutputDto> exchangeCurrency(ExchangeOperationInputDto exchangeOperationInputDto) {
        Optional<ExchangeRateEntity> exchangeRateEntity = findExchangeRate(exchangeOperationInputDto);
        if (exchangeRateEntity.isPresent()) {
            ExchangeRateEntity entity = exchangeRateEntity.get();
            ExchangeOperationIntermediateDto intermediateDto = intermediateDtoBuilder(exchangeOperationInputDto, entity);
            ExchangeOperationOutputDto outputDto = exchangeOperationOutputMapper.mapFrom(intermediateDto);
            return Optional.of(outputDto);
        }
        return Optional.empty();
    }

    private Optional<ExchangeRateEntity> findExchangeRate(ExchangeOperationInputDto exchangeOperationInputDto) {
        Optional<CurrencyEntity> baseCurrency = currenciesDao.findByCode(exchangeOperationInputDto.getBaseCurrencyCode());
        Optional<CurrencyEntity> targetCurrency = currenciesDao.findByCode(exchangeOperationInputDto.getTargetCurrencyCode());
        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            throw new CurrencyNotFoundException();
        }
        Integer baseId = baseCurrency.get().getId();
        Integer targetId = targetCurrency.get().getId();
        Optional<ExchangeRateEntity> exchangeRate = exchangeRateDao.findByBaseIdAndTargetId(baseId, targetId);
        if (exchangeRate.isEmpty()) {
            exchangeRate = findReverseExchangeRate(targetId, baseId);
        }
        if (exchangeRate.isEmpty()) {
            exchangeRate = findCrossExchangeRate(baseId, targetId);
        }
        return exchangeRate;
    }

    private Optional<ExchangeRateEntity> findReverseExchangeRate(Integer targetId, Integer baseId) {
        return exchangeRateDao.findByBaseIdAndTargetId(targetId, baseId);
    }

    private Optional<ExchangeRateEntity> findCrossExchangeRate(Integer baseId, Integer targetId) {
        Integer crossCurrencyId = currenciesDao.findByCode("USD").get().getId();
        Optional<ExchangeRateEntity> baseRate = exchangeRateDao.findByBaseIdAndTargetId(crossCurrencyId, baseId);
        Optional<ExchangeRateEntity> targetRate = exchangeRateDao.findByBaseIdAndTargetId(crossCurrencyId, targetId);
        if (baseRate.isPresent() && targetRate.isPresent()) {
            return Optional.of(calculateExchangeRate(baseRate.get(), targetRate.get()));
        } else {
            return Optional.empty();
        }
    }

    private ExchangeRateEntity calculateExchangeRate(ExchangeRateEntity baseRateEntity, ExchangeRateEntity targetRateEntity) {
        BigDecimal rate = targetRateEntity.getRate().divide(baseRateEntity.getRate(), DECIMAL64);
        return ExchangeRateEntity.builder()
                .baseCurrencyId(baseRateEntity.getTargetCurrencyId())
                .targetCurrencyId(targetRateEntity.getTargetCurrencyId())
                .rate(rate)
                .build();
    }

    private static ExchangeOperationIntermediateDto intermediateDtoBuilder(ExchangeOperationInputDto exchangeOperationInputDto, ExchangeRateEntity entity) {
        BigDecimal rate = entity.getRate();
        BigDecimal amount = new BigDecimal(exchangeOperationInputDto.getAmount());
        return ExchangeOperationIntermediateDto.builder()
                .baseCurrencyId(entity.getBaseCurrencyId())
                .targetCurrencyId(entity.getTargetCurrencyId())
                .rate(rate.setScale(2, HALF_UP))
                .amount(amount.intValue())
                .convertedAmount(rate.multiply(amount).setScale(2, HALF_UP))
                .build();
    }
}

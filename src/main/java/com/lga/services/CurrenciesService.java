package com.lga.services;


import com.lga.dto.CurrencyForSaveDto;
import com.lga.entity.CurrencyEntity;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.lga.util.SingletonConstants.DaoConstants.currenciesDao;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CurrenciesService {
    private static final CurrenciesService INSTANCE = new CurrenciesService();

    public static CurrenciesService getInstance() {
        return INSTANCE;
    }

    public List<CurrencyEntity> findAll() {
        return currenciesDao.findAll();
    }

    public Optional<CurrencyEntity> findByCode(String code) {
        return currenciesDao.findByCode(code);
    }

    public Optional<CurrencyEntity> save(CurrencyForSaveDto currencyForSaveDto) {
        return currenciesDao.save(CurrencyEntity.builder()
                .code(currencyForSaveDto.getCode())
                .fullName(currencyForSaveDto.getFullName())
                .sign(currencyForSaveDto.getSign())
                .build());
    }
}


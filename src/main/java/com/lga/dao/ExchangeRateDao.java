package com.lga.dao;

import com.lga.entity.CurrencyEntity;

import com.lga.entity.ExchangeRateEntity;
import com.lga.util.ConnectionManager;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ExchangeRateDao implements Dao<Integer, ExchangeRateEntity> {
    private static final ExchangeRateDao INSTANCE = new ExchangeRateDao();

    private static final String FIND_BY_ID_SQL = """
            SELECT id,base_currency_id,target_currency_id,rate 
            FROM exchange_rates 
            WHERE id = ?;
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,base_currency_id,target_currency_id,rate
            FROM exchange_rates;
            """;

    private static final String SAVE_SQL = """
            INSERT INTO exchange_rates(base_currency_id, target_currency_id, rate)
            VALUES (?,?,?);
            """;

    private static final String FIND_BY_BASE_AND_TARGET_ID_SQL = """
            SELECT id,base_currency_id,target_currency_id,rate
            FROM exchange_rates
            WHERE base_currency_id = ?
            AND target_currency_id = ?;
            """;


    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    private ExchangeRateEntity findByBaseIdAndTargetId(Integer baseId, Integer targetId) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_BASE_AND_TARGET_ID_SQL)) {
            statement.setInt(1, baseId);
            statement.setInt(2, targetId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return exchangeRateEntityBuilder(resultSet);
            }
            return null;
        }
    }

    @Override
    @SneakyThrows
    public Optional<ExchangeRateEntity> findById(Integer id) {

        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(exchangeRateEntityBuilder(resultSet));
            }
        }
        return Optional.empty();

    }

    @Override
    @SneakyThrows
    public List<ExchangeRateEntity> findAll() {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<ExchangeRateEntity> exchangeRateEntityList = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                exchangeRateEntityList.add(exchangeRateEntityBuilder(resultSet));
            }
            return exchangeRateEntityList;
        }
    }

    @Override
    @SneakyThrows
    public Optional<ExchangeRateEntity> save(ExchangeRateEntity entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {
            if (!isEntityPresent(entity)) {
                statement.setInt(1, entity.getBaseCurrencyId());
                statement.setInt(2, entity.getTargetCurrencyId());
                statement.setBigDecimal(3, entity.getRate());
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();

                resultSet.next();

                entity.setId(resultSet.getInt(1));
                return Optional.of(entity);
            }
            return Optional.empty();

        }
    }

    @Override
    public ExchangeRateEntity update(ExchangeRateEntity entity) {
        return null;
    }

    @SneakyThrows
    private ExchangeRateEntity exchangeRateEntityBuilder(ResultSet resultSet) {
        return ExchangeRateEntity.builder()
                .id(resultSet.getObject("id", Integer.class))
                .baseCurrencyId(resultSet.getObject("base_currency_id", Integer.class))
                .targetCurrencyId(resultSet.getObject("target_currency_id", Integer.class))
                .rate(resultSet.getObject("rate", BigDecimal.class))
                .build();
    }

    private boolean isEntityPresent(ExchangeRateEntity entity) {
        ExchangeRateEntity exchangeRateEntity =
                findByBaseIdAndTargetId(entity.getBaseCurrencyId(), entity.getTargetCurrencyId());
        return exchangeRateEntity != null;
    }

}

package com.lga.dao;

import com.lga.util.ConnectionManager;
import com.lga.entity.CurrencyEntity;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CurrenciesDao implements Dao<Integer, CurrencyEntity> {

    private static final CurrenciesDao INSTANCE = new CurrenciesDao();

    private static final String FIND_ALL_SQL = """
            SELECT id,code,full_name,sign
            FROM currencies;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT id,code,full_name,sign
            FROM currencies
            WHERE id = ?;
            """;
    private static final String SAVE_SQL = """
            INSERT INTO currencies(code, full_name, sign)
            VALUES (?,?,?);
            """;

    private static final String FIND_BY_CODE_SQL = """
            SELECT id,code,full_name,sign
            FROM currencies
            WHERE code = ?;
            """;

    public static CurrenciesDao getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public Optional<CurrencyEntity> findByCode(String code) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CODE_SQL)) {
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(currencyEntityBuilder(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    @SneakyThrows
    public Optional<CurrencyEntity> findById(Integer id) {

        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(currencyEntityBuilder(resultSet));
            }
        }
        return Optional.empty();

    }

    @Override
    @SneakyThrows
    public List<CurrencyEntity> findAll() {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<CurrencyEntity> currencies = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                currencies.add(currencyEntityBuilder(resultSet));
            }
            return currencies;
        }
    }

    @Override
    @SneakyThrows
    public Optional<CurrencyEntity> save(CurrencyEntity entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            if (!isEntityPresent(entity)) {
                statement.setObject(1, entity.getCode());
                statement.setObject(2, entity.getFullName());
                statement.setObject(3, entity.getSign());
                statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();

                resultSet.next();
                entity.setId(resultSet.getObject(1, Integer.class));
                return Optional.of(entity);
            }
            return Optional.empty();

        }
    }

    @Override
    public CurrencyEntity update(CurrencyEntity entity) {
        return null;
    }

    private boolean isEntityPresent(CurrencyEntity entity) {
        Optional<CurrencyEntity> currencyEntity = findByCode(entity.getCode());
        return currencyEntity.isPresent();
    }


    @SneakyThrows
    private CurrencyEntity currencyEntityBuilder(ResultSet resultSet) {
        return CurrencyEntity.builder()
                .id(resultSet.getObject("id", Integer.class))
                .code(resultSet.getObject("code", String.class))
                .fullName(resultSet.getObject("full_name", String.class))
                .sign(resultSet.getObject("sign", String.class))
                .build();
    }

}

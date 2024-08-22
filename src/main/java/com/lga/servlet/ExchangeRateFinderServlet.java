package com.lga.servlet;

import com.lga.dto.ExchangeRateForSaveDto;
import com.lga.services.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebServlet("/exchangeRates/*")
public class ExchangeRateFinderServlet extends HttpServlet {
    private static final String CURRENCY_NOT_FOUND = "Not found. Currency code does not exist";
    private static final String CURRENCY_PAIR_NOT_FOUND = "Not found. Currency pair does not exist";
    private static final String CURRENCY_PAIR_INVALID = "Bad request. Currency pair is invalid";
    private static final String CURRENCY_RATE_ABSENT = "Bad request. Currency rate is absent";

    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equalsIgnoreCase("PATCH")) {
            super.service(req, resp);
        }
        doPatch(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currencyPair = validatePath(req.getRequestURI());
        if (!isPairValid(currencyPair)) {
            resp.sendError(SC_BAD_REQUEST, CURRENCY_PAIR_INVALID);
            return;
        }

        String baseCurrencyCode = currencyPair.substring(0, 3);
        String targetCurrencyCode = currencyPair.substring(3);
        try {
            Optional<String> exchangeRate
                    = exchangeRateService.findExchangeRateByCurrencyCode(baseCurrencyCode, targetCurrencyCode);
            if (exchangeRate.isPresent()) {
                try (PrintWriter printWriter = resp.getWriter()) {
                    resp.setContentType("application/json");
                    printWriter.write(exchangeRate.get());
                }
            } else {
                resp.sendError(SC_NOT_FOUND, CURRENCY_PAIR_NOT_FOUND);
            }
        } catch (NoSuchElementException e) {
            resp.sendError(SC_NOT_FOUND, CURRENCY_NOT_FOUND);
        }
    }

    private void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String currencyPair = validatePath(req.getRequestURI());
        if (!isPairValid(currencyPair)) {
            resp.sendError(SC_BAD_REQUEST, CURRENCY_PAIR_INVALID);
            return;
        }
        String parameter = req.getReader().readLine();
        if (parameter == null || !parameter.contains("rate")) {
            resp.sendError(SC_BAD_REQUEST, CURRENCY_RATE_ABSENT);
            return;
        }
        ExchangeRateForSaveDto exchangeRateForSaveDto = ExchangeRateForSaveDto.builder()
                .baseCurrencyCode(currencyPair.substring(0, 3))
                .targetCurrencyCode(currencyPair.substring(3))
                .rate(parameter.replace("rate=", ""))
                .build();
        Optional<String> updatedExchangerRate = exchangeRateService.update(exchangeRateForSaveDto);
        if (updatedExchangerRate.isPresent()){
            try (PrintWriter printWriter = resp.getWriter()) {
                //Filter "app/json"
                printWriter.write(updatedExchangerRate.get());
            }
        }else{
            resp.sendError(SC_NOT_FOUND,CURRENCY_PAIR_NOT_FOUND);
        }
    }

    private String validatePath(String path) {
        return path.replace("/exchangeRates/", "").toUpperCase();
    }

    private boolean isPairValid(String currencyPair) {
        return currencyPair.length() == 6;
    }
}

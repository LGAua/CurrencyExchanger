package com.lga.servlet;

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
    private static final String CURRENCY_PAIR_INVALID = "Bad request. Currency pair is invalid";
    private static final String CURRENCY_PAIR_NOT_FOUND = "Not found. Currency pair does not exist";

    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String currencyPair = uri.replace("/exchangeRates/", "").toUpperCase();
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

    private boolean isPairValid(String currencyPair) {
        return currencyPair.length() == 6;
    }
}

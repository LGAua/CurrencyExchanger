package com.lga.servlet;

import com.lga.entity.CurrencyEntity;
import com.lga.services.CurrenciesService;
import com.lga.util.JsonConverter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebServlet("/currency/*")
public class CurrencyFinderServlet extends HttpServlet {
    private static final String CURRENCY_NOT_FOUND = "Not found. Currency does not exist";

    private final CurrenciesService currenciesService = CurrenciesService.getInstance();
    private final JsonConverter jsonConverter = JsonConverter.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String currencyCode = uri.replace("/currency/", "").toUpperCase();
        Optional<CurrencyEntity> currencyEntity = currenciesService.findByCode(currencyCode);
        if (currencyEntity.isPresent()) {
            try (PrintWriter printWriter = resp.getWriter()) {
                printWriter.write(jsonConverter.convertToJson(currencyEntity.get()));
            }
        } else {
            resp.sendError(SC_NOT_FOUND, CURRENCY_NOT_FOUND);
        }
    }
}

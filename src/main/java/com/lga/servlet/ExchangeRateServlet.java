package com.lga.servlet;

import com.lga.dto.ExchangeRateForSaveDto;
import com.lga.exceptions.CurrencyNotFoundException;
import com.lga.services.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.*;


@WebServlet("/exchangeRates")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private static final String FIELD_IS_EMPTY = "Bad request. One of required fields is empty";
    private static final String CURRENCY_ALREADY_EXISTS = "Conflict. Exchange rate already exists";
    private static final String CURRENCY_NOT_FOUND = "Not found. Currency does not exist";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        List<String> exchangeRateDtoList = exchangeRateService.findAll();
        try (PrintWriter writer = resp.getWriter()) {
            for (String exchangeRateDto : exchangeRateDtoList) {
                writer.write(exchangeRateDto);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateForSaveDto exchangeRateForSaveDto = ExchangeRateForSaveDto.builder()
                .baseCurrencyCode(req.getParameter("baseCurrencyCode"))
                .targetCurrencyCode(req.getParameter("targetCurrencyCode"))
                .rate(req.getParameter("rate")).build();
        if (!isValid(exchangeRateForSaveDto)) {
            resp.sendError(SC_BAD_REQUEST, FIELD_IS_EMPTY);
            return;
        }

        Optional<String> exchangeRateDto;
        try {
            exchangeRateDto = exchangeRateService.save(exchangeRateForSaveDto);
        } catch (CurrencyNotFoundException e) {
            resp.sendError(SC_NOT_FOUND, CURRENCY_NOT_FOUND);
            return;
        }

        if (exchangeRateDto.isPresent()) {
            try (PrintWriter writer = resp.getWriter()) {
                resp.setContentType("application/json");
                writer.write(exchangeRateDto.get());
            }
        } else {
            resp.sendError(SC_CONFLICT, CURRENCY_ALREADY_EXISTS);
        }

    }

    private boolean isValid(ExchangeRateForSaveDto entity) {
        if (entity.getRate() == null || entity.getRate().isEmpty()
                || entity.getBaseCurrencyCode() == null || entity.getBaseCurrencyCode().isEmpty()
                || entity.getTargetCurrencyCode() == null || entity.getTargetCurrencyCode().isEmpty()) {
            return false;
        }
        return true;
    }
}

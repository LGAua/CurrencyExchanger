package com.lga.servlet;

import com.lga.dto.ExchangeRateDto;
import com.lga.dto.ExchangeRateForSaveDto;
import com.lga.exceptions.CurrencyNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import static com.lga.util.HttpResponseTextConstants.FIELD_IS_EMPTY;
import static com.lga.util.HttpResponseTextConstants.CURRENCY_NOT_FOUND;
import static com.lga.util.HttpResponseTextConstants.EXCHANGE_RATE_ALREADY_EXISTS;
import static com.lga.util.SingletonConstants.ServiceConstants.exchangeRateService;
import static com.lga.util.SingletonConstants.UtilConstant.jsonConverter;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;


@WebServlet("/exchangeRates")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRateDto> exchangeRateDtoList = exchangeRateService.findAll();
        try (PrintWriter writer = resp.getWriter()) {
            for (ExchangeRateDto exchangeRateDto : exchangeRateDtoList) {
                writer.write(jsonConverter.convertToJson(exchangeRateDto));
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

        Optional<ExchangeRateDto> exchangeRateDto;
        try {
            exchangeRateDto = exchangeRateService.save(exchangeRateForSaveDto);
        } catch (CurrencyNotFoundException e) {
            resp.sendError(SC_NOT_FOUND, CURRENCY_NOT_FOUND);
            return;
        }

        if (exchangeRateDto.isPresent()) {
            try (PrintWriter writer = resp.getWriter()) {
                writer.write(jsonConverter.convertToJson(exchangeRateDto.get()));
            }
        } else {
            resp.sendError(SC_CONFLICT, EXCHANGE_RATE_ALREADY_EXISTS);
        }
    }

    private boolean isValid(ExchangeRateForSaveDto entity) {
        if (entity.getRate() == null || entity.getRate().isEmpty() || Integer.parseInt(entity.getRate()) < 0
                || entity.getBaseCurrencyCode() == null || entity.getBaseCurrencyCode().isEmpty()
                || entity.getTargetCurrencyCode() == null || entity.getTargetCurrencyCode().isEmpty()) {
            return false;
        }
        return true;
    }
}

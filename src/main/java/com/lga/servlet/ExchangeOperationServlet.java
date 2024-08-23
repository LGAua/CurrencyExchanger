package com.lga.servlet;

import com.lga.dto.ExchangeOperationInputDto;
import com.lga.dto.ExchangeOperationOutputDto;
import com.lga.exceptions.CurrencyNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static com.lga.util.HttpResponseTextConstants.*;
import static com.lga.util.SingletonConstants.ServiceConstants.exchangeOperationService;
import static com.lga.util.SingletonConstants.UtilConstant.jsonConverter;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebServlet("/exchange")
public class ExchangeOperationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeOperationInputDto exchangeOperationInputDto = ExchangeOperationInputDto.builder()
                .baseCurrencyCode(req.getParameter("from"))
                .targetCurrencyCode(req.getParameter("to"))
                .amount(req.getParameter("amount"))
                .build();
        if (!isValid(exchangeOperationInputDto)) {
            resp.sendError(SC_BAD_REQUEST, REQUEST_FIELDS_INVALID);
            return;
        }
        try {
            Optional<ExchangeOperationOutputDto> exchangeOperationResult = exchangeOperationService.exchangeCurrency(exchangeOperationInputDto);
            if (exchangeOperationResult.isPresent()) {
                try (PrintWriter printWriter = resp.getWriter()) {
                    printWriter.write(jsonConverter.convertToJson(exchangeOperationResult.get()));
                }
            } else {
                resp.sendError(SC_NOT_FOUND, CURRENCY_PAIR_NOT_FOUND);
            }
        } catch (CurrencyNotFoundException e) {
            resp.sendError(SC_NOT_FOUND, CURRENCY_NOT_FOUND);
        }
    }

    private boolean isValid(ExchangeOperationInputDto inputDto) {
        if (inputDto.getTargetCurrencyCode().length() != 3
                || inputDto.getBaseCurrencyCode().length() != 3
                || inputDto.getTargetCurrencyCode().equals(inputDto.getBaseCurrencyCode())
                || inputDto.getAmount().isEmpty()
                || Integer.parseInt(inputDto.getAmount()) < 1) {
            return false;
        }
        return true;
    }
}

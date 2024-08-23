package com.lga.servlet;

import com.lga.dto.ExchangeOperationInputDto;
import com.lga.dto.ExchangeOperationOutputDto;
import com.lga.exceptions.CurrencyNotFoundException;
import com.lga.exceptions.ExchangeRatePairNotFoundException;
import com.lga.services.ExchangeOperationService;
import com.lga.util.JsonConverter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static com.lga.util.Constants.ServiceConstants.exchangeOperationService;
import static com.lga.util.Constants.UtilConstant.jsonConverter;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebServlet("/exchange")
public class ExchangeOperationServlet extends HttpServlet {
    private static final String CURRENCY_NOT_FOUND = "Not found. Currency code does not exist";
    private static final String CURRENCY_PAIR_NOT_FOUND = "Not found. Currency pair does not exist";
    private static final String REQUEST_FIELDS_INVALID = "Bad request. Currency pair is invalid";


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
            ExchangeOperationOutputDto exchangeOperationResult = exchangeOperationService.makeCurrencyExchange(exchangeOperationInputDto);
            try (PrintWriter printWriter = resp.getWriter()) {
                printWriter.write(jsonConverter.convertToJson(exchangeOperationResult));
            }
        } catch (CurrencyNotFoundException e) {
            resp.sendError(SC_NOT_FOUND, CURRENCY_NOT_FOUND);
        } catch (ExchangeRatePairNotFoundException e) {
            resp.sendError(SC_NOT_FOUND, CURRENCY_PAIR_NOT_FOUND);
        }
    }

    private boolean isValid(ExchangeOperationInputDto inputDto) {
        if (inputDto.getTargetCurrencyCode().length() != 3
                || inputDto.getBaseCurrencyCode().length() != 3
                || inputDto.getAmount().isEmpty() || Integer.parseInt(inputDto.getAmount()) < 1) {
            return false;
        }
        return true;
    }
}

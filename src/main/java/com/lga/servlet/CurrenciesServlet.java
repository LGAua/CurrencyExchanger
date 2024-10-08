package com.lga.servlet;

import com.lga.dto.CurrencyForSaveDto;
import com.lga.entity.CurrencyEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import static com.lga.util.HttpResponseTextConstants.CURRENCY_ALREADY_EXISTS;
import static com.lga.util.HttpResponseTextConstants.FIELD_IS_EMPTY;
import static com.lga.util.SingletonConstants.ServiceConstants.currenciesService;
import static com.lga.util.SingletonConstants.UtilConstant.jsonConverter;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CurrencyEntity> currencyEntityList = currenciesService.findAll();
        try (PrintWriter writer = resp.getWriter()) {
            for (CurrencyEntity currencyEntity : currencyEntityList) {
                System.out.println(currencyEntity);
                writer.write(jsonConverter.convertToJson(currencyEntity));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyForSaveDto currencyForSaveDto = CurrencyForSaveDto.builder()
                .fullName(req.getParameter("name"))
                .code(req.getParameter("code"))
                .sign(req.getParameter("sign"))
                .build();
        if (!isValid(currencyForSaveDto)) {
            resp.sendError(SC_BAD_REQUEST, FIELD_IS_EMPTY);
            return;
        }
        Optional<CurrencyEntity> currencyEntityJson = currenciesService.save(currencyForSaveDto);
        if (currencyEntityJson.isPresent()) {
            try (PrintWriter writer = resp.getWriter()) {
                writer.write(jsonConverter.convertToJson(currencyEntityJson.get()));
            }
        } else {
            resp.sendError(SC_CONFLICT, CURRENCY_ALREADY_EXISTS);
        }
    }

    private boolean isValid(CurrencyForSaveDto currencyForSaveDto) {
        if (currencyForSaveDto.getCode() == null || currencyForSaveDto.getCode().length() != 3
                || currencyForSaveDto.getFullName() == null || currencyForSaveDto.getFullName().isEmpty()
                || currencyForSaveDto.getSign() == null || currencyForSaveDto.getSign().isEmpty()) {
            return false;
        }
        return true;
    }
}

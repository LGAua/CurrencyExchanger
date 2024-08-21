package com.lga.servlet;

import com.lga.dto.CurrencyForSaveDto;
import com.lga.services.CurrenciesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrenciesService currenciesService = CurrenciesService.getInstance();
    private static final String FIELD_IS_EMPTY = "Bad request. One of required fields is empty";
    private static final String CURRENCY_ALREADY_EXISTS = "Conflict. Currency with such code already exists";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        List<String> currencyEntityList = currenciesService.findAll();
        try (PrintWriter writer = resp.getWriter()) {
            for (String currencyEntity : currencyEntityList) {
                System.out.println(currencyEntity);
                writer.write(currencyEntity);
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
        Optional<String> currencyEntityJson = currenciesService.save(currencyForSaveDto);
        if (currencyEntityJson.isPresent()) {
            resp.setContentType("application/json");
            try (PrintWriter writer = resp.getWriter()) {
                writer.write(currencyEntityJson.get());
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

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
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        List<String> exchangeRateDtoList = exchangeRateService.findAll();
        try (PrintWriter writer = resp.getWriter()) {
            for (String exchangeRateDto : exchangeRateDtoList) {
                writer.write(exchangeRateDto.toString());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String exchangeRateDto = exchangeRateService.save(ExchangeRateForSaveDto.builder()
                .baseCurrencyCode(req.getParameter("baseCurrencyCode"))
                .targetCurrencyCode(req.getParameter("targetCurrencyCode"))
                .rate(req.getParameter("rate")).build());
        try (PrintWriter writer = resp.getWriter()) {
            writer.write(exchangeRateDto);
        }
    }
}

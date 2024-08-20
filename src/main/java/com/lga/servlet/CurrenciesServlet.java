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

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrenciesService currenciesService = CurrenciesService.getInstance();

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
        String currencyEntityJson = currenciesService.save(CurrencyForSaveDto.builder()
                .fullName(req.getParameter("name"))
                .code(req.getParameter("code"))
                .sign(req.getParameter("sign"))
                .build());
        try (PrintWriter writer = resp.getWriter()) {
            writer.write(currencyEntityJson);
        }
    }
}

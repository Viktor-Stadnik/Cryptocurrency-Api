package com.cryptocurrencyapi.service.impl;

import com.cryptocurrencyapi.exception.DataProcessingException;
import com.cryptocurrencyapi.model.Cryptocurrency;
import com.cryptocurrencyapi.service.CryptocurrencyReportService;
import com.cryptocurrencyapi.service.CryptocurrencyService;
import java.math.BigDecimal;
import java.util.Comparator;
import org.springframework.stereotype.Service;

@Service
public class CryptocurrencyReportServiceImpl implements CryptocurrencyReportService {
    private final CryptocurrencyService cryptocurrencyService;

    public CryptocurrencyReportServiceImpl(CryptocurrencyService cryptocurrencyService) {
        this.cryptocurrencyService = cryptocurrencyService;
    }

    @Override
    public String[] getCryptocurrencyReport(String currency) {
        String upCurrency = currency.toUpperCase();
        BigDecimal maxPrice = cryptocurrencyService.findAll().stream()
                .filter(m -> m.getMarketSymbol().equals(upCurrency))
                .map(Cryptocurrency::getPrice)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new DataProcessingException("Can't find max price"
                        + " for currency: " + upCurrency));
        BigDecimal minPrice = cryptocurrencyService.findAll().stream()
                .filter(m -> m.getMarketSymbol().equals(upCurrency))
                .map(Cryptocurrency::getPrice)
                .min(Comparator.naturalOrder())
                .orElseThrow(() -> new DataProcessingException("Can't find min price"
                        + " for currency: " + upCurrency));
        String value = cryptocurrencyService.findAll().stream()
                .filter(m -> m.getPrice().equals(maxPrice))
                .map(Cryptocurrency::getMarketSymbol)
                .findFirst()
                .orElseThrow(() -> new DataProcessingException("Can't find name"
                        + " for currency: " + upCurrency));
        return new String[]{value, minPrice.toString(), maxPrice.toString()};
    }
}

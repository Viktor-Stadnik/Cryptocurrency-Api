package com.cryptocurrencyapi.service.impl;

import com.cryptocurrencyapi.exception.DataProcessingException;
import com.cryptocurrencyapi.model.Cryptocurrency;
import com.cryptocurrencyapi.repository.CryptocurrencyRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CryptocurrencyServiceImplTest {
    @InjectMocks
    private CryptocurrencyServiceImpl service;

    @Mock
    private CryptocurrencyRepository repository;

    @Test
    public void getMaxCryptocurrency_DataProcessing() {
        Mockito.when(repository.findAllByMarketSymbolOrderByPriceDesc("INVALID"))
                .thenReturn(List.of());
        try {
            Cryptocurrency actual = service.findByMarketSymbolOrderByPriceMax("INVALID");
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't get max currency, the name: "
                    + "INVALID" + " not correct", e.getMessage());
            return;
        }
        Assertions.fail("Expected receive DataProcessingException");
    }

    @Test
    public void getMaxSingleUpperNameCryptocurrencyFromList() {
        Mockito.when(repository.findAllByMarketSymbolOrderByPriceDesc("BTC"))
                .thenReturn(List.of(new Cryptocurrency("5", "BTC",
                        BigDecimal.valueOf(1500), LocalDateTime.now())));
        Cryptocurrency actual = service.findByMarketSymbolOrderByPriceMax("Btc");
        Assertions.assertEquals(BigDecimal.valueOf(1500), actual.getPrice());
        Assertions.assertEquals("BTC", actual.getMarketSymbol());
    }

    @Test
    public void getMinCryptocurrency_DataProcessing() {
        Mockito.when(repository.findAllByMarketSymbolOrderByPriceAsc("INVALID"))
                .thenReturn(List.of());
        try {
            Cryptocurrency actual = service.findByMarketSymbolOrderByPriceMin("INVALID");
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't get min currency, the name: "
                    + "INVALID" + " not correct", e.getMessage());
            return;
        }
        Assertions.fail("Expected receive DataProcessingException");
    }

    @Test
    public void getMinSingleUpperNameCryptocurrencyFromList() {
        Mockito.when(repository.findAllByMarketSymbolOrderByPriceAsc("BTC"))
                .thenReturn(List.of(new Cryptocurrency("5", "BTC",
                        BigDecimal.valueOf(1000), LocalDateTime.now())));
        Cryptocurrency actual = service.findByMarketSymbolOrderByPriceMin("Btc");
        Assertions.assertEquals(BigDecimal.valueOf(1000), actual.getPrice());
        Assertions.assertEquals("BTC", actual.getMarketSymbol());
    }
}

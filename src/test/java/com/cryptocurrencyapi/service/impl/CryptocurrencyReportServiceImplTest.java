package com.cryptocurrencyapi.service.impl;

import com.cryptocurrencyapi.exception.DataProcessingException;
import com.cryptocurrencyapi.model.Cryptocurrency;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CryptocurrencyReportServiceImplTest {
    private List<Cryptocurrency> cryptocurrencyList;

    @InjectMocks
    private CryptocurrencyReportServiceImpl reportService;

    @Mock
    private CryptocurrencyServiceImpl service;

    @BeforeEach
    void setUp() {
        cryptocurrencyList = List.of(
                new Cryptocurrency("1", "BTC", BigDecimal.valueOf(1200), LocalDateTime.now()),
                new Cryptocurrency("2", "BTC", BigDecimal.valueOf(1500), LocalDateTime.now()),
                new Cryptocurrency("3", "BTC", BigDecimal.valueOf(1000), LocalDateTime.now()),
                new Cryptocurrency("4", "ETH", BigDecimal.valueOf(800), LocalDateTime.now()),
                new Cryptocurrency("5", "ETH", BigDecimal.valueOf(950), LocalDateTime.now()),
                new Cryptocurrency("6", "ETH", BigDecimal.valueOf(1300), LocalDateTime.now()),
                new Cryptocurrency("7", "XRP", BigDecimal.valueOf(200), LocalDateTime.now()),
                new Cryptocurrency("8", "XRP", BigDecimal.valueOf(450), LocalDateTime.now()),
                new Cryptocurrency("9", "XRP", BigDecimal.valueOf(600), LocalDateTime.now()));
    }

    @Test
    public void getCryptocurrencyReport_DataProcessing() {
        Mockito.when(service.findAll()).thenReturn(cryptocurrencyList);

        try {
            String[] actual = reportService.getCryptocurrencyReport("INVALID");

        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't find max price"
                    + " for currency: " + "INVALID", e.getMessage());
            return;
        }
        Assertions.fail("Expected receive DataProcessingException");
    }

    @Test
    public void getReportBtcWithMinMaxPrices() {
        Mockito.when(service.findAll()).thenReturn(cryptocurrencyList);
        String[] actual = reportService.getCryptocurrencyReport("Btc");
        String[] expect = new String[]{"BTC", "1000", "1500"};
        Assertions.assertEquals(Arrays.toString(expect), Arrays.toString(actual));
    }

    @Test
    public void getReportEthWithMinMaxPrices() {
        Mockito.when(service.findAll()).thenReturn(cryptocurrencyList);
        String[] actual = reportService.getCryptocurrencyReport("Eth");
        String[] expect = new String[]{"ETH", "800", "1300"};
        Assertions.assertEquals(Arrays.toString(expect), Arrays.toString(actual));
    }

    @Test
    public void getReportXrpWithMinMaxPrices() {
        Mockito.when(service.findAll()).thenReturn(cryptocurrencyList);
        String[] actual = reportService.getCryptocurrencyReport("xrp");
        String[] expect = new String[]{"XRP", "200", "600"};
        Assertions.assertEquals(Arrays.toString(expect), Arrays.toString(actual));
    }
}

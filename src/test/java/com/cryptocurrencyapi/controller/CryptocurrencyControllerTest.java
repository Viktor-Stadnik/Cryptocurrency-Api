package com.cryptocurrencyapi.controller;

import static org.mockito.ArgumentMatchers.any;

import com.cryptocurrencyapi.model.Cryptocurrency;
import com.cryptocurrencyapi.service.CryptocurrencyService;
import com.cryptocurrencyapi.service.CryptocurrencyWriterService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class CryptocurrencyControllerTest {
    private static final String PATH_REPORT = "src/main/resources/report.csv";

    @MockBean
    private CryptocurrencyService service;

    @MockBean
    private CryptocurrencyWriterService writerService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void shouldReturnCurrencyWithMinPriceByName() {
        Mockito.when(service.findByMarketSymbolOrderByPriceMin("BTC"))
                .thenReturn(new Cryptocurrency("5", "BTC",
                        BigDecimal.valueOf(1000), LocalDateTime.now()));

        RestAssuredMockMvc.given()
                .queryParam("name", "BTC")
                .when()
                .get("/cryptocurrencies/minprice")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo("5"))
                .body("marketSymbol", Matchers.equalTo("BTC"))
                .body("price", Matchers.equalTo(1000))
                .body("createdAt", Matchers.notNullValue());
    }

    @Test
    public void shouldReturnCurrencyWithMaxPriceByName() {
        Mockito.when(service.findByMarketSymbolOrderByPriceMax("ETH"))
                .thenReturn(new Cryptocurrency("3", "ETH",
                        BigDecimal.valueOf(1500), LocalDateTime.now()));

        RestAssuredMockMvc.given()
                .queryParam("name", "ETH")
                .when()
                .get("/cryptocurrencies/maxprice")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo("3"))
                .body("marketSymbol", Matchers.equalTo("ETH"))
                .body("price", Matchers.equalTo(1500))
                .body("createdAt", Matchers.notNullValue());
    }

    @Test
    public void shouldReturnAllCurrencyByName() {
        List<Cryptocurrency> mockCurrency = List.of(
                // id 2
                new Cryptocurrency("1", "BTC",
                        BigDecimal.valueOf(1300), LocalDateTime.now()),
                new Cryptocurrency("2", "BTC",
                        BigDecimal.valueOf(1000), LocalDateTime.now()),
                new Cryptocurrency("3", "BTC",
                        BigDecimal.valueOf(1500), LocalDateTime.now())
        );

        Mockito.when(service.findAllByMarketSymbolOrderByPriceAsc(any(), any()))
                .thenReturn(mockCurrency);

        RestAssuredMockMvc.given()
                .queryParam("name", "BTC")
                .when()
                .get("/cryptocurrencies")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(3))
                .body("[0].id", Matchers.equalTo("1"))
                .body("[0].marketSymbol", Matchers.equalTo("BTC"))
                .body("[0].price", Matchers.equalTo(1300))
                .body("[0].createdAt", Matchers.notNullValue())
                .body("[1].id", Matchers.equalTo("2"))
                .body("[1].marketSymbol", Matchers.equalTo("BTC"))
                .body("[1].price", Matchers.equalTo(1000))
                .body("[1].createdAt", Matchers.notNullValue())
                .body("[2].marketSymbol", Matchers.equalTo("BTC"))
                .body("[2].price", Matchers.equalTo(1500))
                .body("[2].id", Matchers.equalTo("3"))
                .body("[2].createdAt", Matchers.notNullValue());
    }

    @Test
    public void shouldWritePricesToFile() {
        String message = "The report was created, see: src/main/resources/";

        Mockito.doNothing().when(writerService).writePricesToFile(PATH_REPORT);
        RestAssuredMockMvc.when()
                .get("/cryptocurrencies/csv")
                .then()
                .statusCode(200)
                .body(Matchers.is(message));
    }
}

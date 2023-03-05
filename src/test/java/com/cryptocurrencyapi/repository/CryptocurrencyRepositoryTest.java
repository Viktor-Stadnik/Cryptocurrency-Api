package com.cryptocurrencyapi.repository;

import com.cryptocurrencyapi.model.Cryptocurrency;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
class CryptocurrencyRepositoryTest {

    @Container
    private static MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:4.4.2");

    @Autowired
    private CryptocurrencyRepository repository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeAll
    static void beforeAll() {
        mongoDBContainer.start();
    }

    @BeforeEach
    void setUp() {
        this.repository.save(new Cryptocurrency("4", "BTC",
                BigDecimal.valueOf(1300), LocalDateTime.now()));
        this.repository.save(new Cryptocurrency("1", "BTC",
                BigDecimal.valueOf(1000), LocalDateTime.now()));
        this.repository.save(new Cryptocurrency("2", "BTC",
                BigDecimal.valueOf(1500), LocalDateTime.now()));
        this.repository.save(new Cryptocurrency("3", "BTC",
                BigDecimal.valueOf(1200), LocalDateTime.now()));
        this.repository.save(new Cryptocurrency("5", "ETH",
                BigDecimal.valueOf(900), LocalDateTime.now()));
    }

    @Test
    public void returnCurrencyWithMaxPrice() {
        List<Cryptocurrency> actual = repository.findAllByMarketSymbolOrderByPriceDesc("BTC");
        Assertions.assertEquals(4, actual.size());
        Assertions.assertEquals(BigDecimal.valueOf(1500), actual.get(0).getPrice());
    }

    @Test
    public void returnCurrencyWithMinPrice() {
        List<Cryptocurrency> actual = repository.findAllByMarketSymbolOrderByPriceAsc("BTC");
        Assertions.assertEquals(4, actual.size());
        Assertions.assertEquals(BigDecimal.valueOf(1000), actual.get(0).getPrice());
    }

    @Test
    public void returnCurrencyWithMinPriceAndPageMax() {
        PageRequest pageRequest = PageRequest.ofSize(5);
        List<Cryptocurrency> actual = repository
                .findAllByMarketSymbolOrderByPriceAsc("BTC", pageRequest);
        Assertions.assertEquals(4, actual.size());
        Assertions.assertEquals(BigDecimal.valueOf(1000), actual.get(0).getPrice());
    }

    @Test
    public void returnCurrencyWithMinPriceAndPageOne() {
        PageRequest pageRequest = PageRequest.ofSize(1);
        List<Cryptocurrency> actual = repository
                .findAllByMarketSymbolOrderByPriceAsc("BTC", pageRequest);
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(BigDecimal.valueOf(1000), actual.get(0).getPrice());
    }

    @AfterEach
    void tearDown() {
        this.repository.deleteAll();
    }
}

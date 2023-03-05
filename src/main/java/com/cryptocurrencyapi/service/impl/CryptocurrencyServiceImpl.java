package com.cryptocurrencyapi.service.impl;

import com.cryptocurrencyapi.dto.external.ApiResponseDto;
import com.cryptocurrencyapi.dto.mapper.CryptocurrencyMapper;
import com.cryptocurrencyapi.exception.DataProcessingException;
import com.cryptocurrencyapi.model.Cryptocurrency;
import com.cryptocurrencyapi.repository.CryptocurrencyRepository;
import com.cryptocurrencyapi.service.CryptocurrencyService;
import com.cryptocurrencyapi.service.HttpClient;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CryptocurrencyServiceImpl implements CryptocurrencyService {
    private final HttpClient httpClient;
    private final CryptocurrencyRepository repository;
    private final CryptocurrencyMapper mapper;

    public CryptocurrencyServiceImpl(HttpClient httpClient,
                                     CryptocurrencyRepository cryptocurrencyRepository,
                                     CryptocurrencyMapper mapper) {
        this.httpClient = httpClient;
        this.repository = cryptocurrencyRepository;
        this.mapper = mapper;
    }

    @Scheduled(cron = "*/10 * * * * ?")
    @Override
    public void syncExternalCryptocurrency() {
        log.info("************************ " + LocalDateTime.now());
        ApiResponseDto apiResponseDtoBtc = httpClient.get("https://cex.io/api/last_price/BTC/USD",
                ApiResponseDto.class);
        ApiResponseDto apiResponseDtoEth = httpClient.get("https://cex.io/api/last_price/ETH/USD",
                ApiResponseDto.class);
        ApiResponseDto apiResponseDtoXrp = httpClient.get("https://cex.io/api/last_price/XRP/USD",
                ApiResponseDto.class);
        saveToDb(apiResponseDtoBtc);
        saveToDb(apiResponseDtoEth);
        saveToDb(apiResponseDtoXrp);
    }

    @Override
    public Cryptocurrency findByMarketSymbolOrderByPriceMax(String marketSymbol) {
        return repository.findAllByMarketSymbolOrderByPriceDesc(marketSymbol.toUpperCase()).stream()
                .findFirst().orElseThrow(()
                        -> new DataProcessingException("Can't get max currency, the name: "
                        + marketSymbol + " not correct"));
    }

    @Override
    public Cryptocurrency findByMarketSymbolOrderByPriceMin(String marketSymbol) {
        return repository.findAllByMarketSymbolOrderByPriceAsc(marketSymbol.toUpperCase()).stream()
                .findFirst().orElseThrow(()
                        -> new DataProcessingException("Can't get min currency, the name: "
                        + marketSymbol + " not correct"));
    }

    @Override
    public List<Cryptocurrency> findAllByMarketSymbolOrderByPriceAsc(String marketSymbol,
                                                                     Pageable pageable) {
        validName(marketSymbol);
        return repository
                .findAllByMarketSymbolOrderByPriceAsc(marketSymbol.toUpperCase(), pageable);
    }

    @Override
    public List<Cryptocurrency> findAll() {
        return repository.findAll();
    }

    void saveToDb(ApiResponseDto apiResponseDto) {
        repository.save(mapper.parseApiCryptocurrencyResponseDto(apiResponseDto));
    }

    private void validName(String marketSymbol) {
        List<String> currencyList = List.of("ETH", "BTC", "XRP");
        if (!currencyList.contains(marketSymbol.toUpperCase())) {
            throw new DataProcessingException("The currency name: "
                    + marketSymbol + " is not correct");
        }
    }
}

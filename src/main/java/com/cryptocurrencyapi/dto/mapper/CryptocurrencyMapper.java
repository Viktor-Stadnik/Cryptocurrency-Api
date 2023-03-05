package com.cryptocurrencyapi.dto.mapper;

import com.cryptocurrencyapi.dto.CryptocurrencyResponseDto;
import com.cryptocurrencyapi.dto.external.ApiResponseDto;
import com.cryptocurrencyapi.model.Cryptocurrency;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class CryptocurrencyMapper {
    public Cryptocurrency parseApiCryptocurrencyResponseDto(ApiResponseDto dto) {
        Cryptocurrency cryptocurrency = new Cryptocurrency();
        cryptocurrency.setMarketSymbol(dto.getCurr1());
        cryptocurrency.setPrice(new BigDecimal(dto.getLprice()));
        cryptocurrency.setCreatedAt(LocalDateTime.now());
        return cryptocurrency;
    }

    public CryptocurrencyResponseDto toResponseDto(Cryptocurrency cryptocurrency) {
        CryptocurrencyResponseDto responseDto = new CryptocurrencyResponseDto();
        responseDto.setId(cryptocurrency.getId());
        responseDto.setMarketSymbol(cryptocurrency.getMarketSymbol());
        responseDto.setPrice(cryptocurrency.getPrice());
        responseDto.setCreatedAt(cryptocurrency.getCreatedAt());
        return responseDto;
    }
}

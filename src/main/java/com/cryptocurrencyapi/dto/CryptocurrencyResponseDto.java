package com.cryptocurrencyapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CryptocurrencyResponseDto {
    private String id;
    private String marketSymbol;
    private BigDecimal price;
    private LocalDateTime createdAt;
}

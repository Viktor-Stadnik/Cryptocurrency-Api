package com.cryptocurrencyapi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cryptocurrencies")
public class Cryptocurrency {
    @Id
    private String id;
    private String marketSymbol;
    private BigDecimal price;
    private LocalDateTime createdAt;
}

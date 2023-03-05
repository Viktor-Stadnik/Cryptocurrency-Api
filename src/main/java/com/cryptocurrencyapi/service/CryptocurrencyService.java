package com.cryptocurrencyapi.service;

import com.cryptocurrencyapi.model.Cryptocurrency;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CryptocurrencyService {
    void syncExternalCryptocurrency();

    Cryptocurrency findByMarketSymbolOrderByPriceMax(String marketSymbol);

    Cryptocurrency findByMarketSymbolOrderByPriceMin(String marketSymbol);

    List<Cryptocurrency> findAllByMarketSymbolOrderByPriceAsc(String marketSymbol,
                                                              Pageable pageable);

    List<Cryptocurrency> findAll();
}

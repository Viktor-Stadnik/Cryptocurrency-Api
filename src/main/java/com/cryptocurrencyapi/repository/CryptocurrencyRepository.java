package com.cryptocurrencyapi.repository;

import com.cryptocurrencyapi.model.Cryptocurrency;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CryptocurrencyRepository extends MongoRepository<Cryptocurrency, String> {

    List<Cryptocurrency> findAllByMarketSymbolOrderByPriceDesc(String marketSymbol);

    List<Cryptocurrency> findAllByMarketSymbolOrderByPriceAsc(String marketSymbol);

    List<Cryptocurrency> findAllByMarketSymbolOrderByPriceAsc(String marketSymbol,
                                                              Pageable pageable);
}

package com.cryptocurrencyapi.controller;

import com.cryptocurrencyapi.dto.CryptocurrencyResponseDto;
import com.cryptocurrencyapi.dto.mapper.CryptocurrencyMapper;
import com.cryptocurrencyapi.service.CryptocurrencyService;
import com.cryptocurrencyapi.service.CryptocurrencyWriterService;
import com.cryptocurrencyapi.util.SortCryptocurrencyUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cryptocurrencies")
public class CryptocurrencyController {
    private static final String PATH_REPORT = "src/main/resources/report.csv";
    private final CryptocurrencyService cryptocurrencyService;
    private final CryptocurrencyMapper mapper;
    private final SortCryptocurrencyUtil sortCryptocurrencyUtil;
    private final CryptocurrencyWriterService writerService;

    public CryptocurrencyController(CryptocurrencyService cryptocurrencyService,
                                    CryptocurrencyMapper mapper,
                                    SortCryptocurrencyUtil sortCryptocurrencyUtil,
                                    CryptocurrencyWriterService writerService) {
        this.cryptocurrencyService = cryptocurrencyService;
        this.mapper = mapper;
        this.sortCryptocurrencyUtil = sortCryptocurrencyUtil;
        this.writerService = writerService;
    }

    @GetMapping("/minprice")
    public CryptocurrencyResponseDto getMinPrice(@RequestParam("name")
                                                         String marketSymbol) {
        return mapper.toResponseDto(cryptocurrencyService
                .findByMarketSymbolOrderByPriceMin(marketSymbol));
    }

    @GetMapping("/maxprice")
    public CryptocurrencyResponseDto getMaxPrice(@RequestParam("name")
                                                         String marketSymbol) {
        return mapper.toResponseDto(cryptocurrencyService
                .findByMarketSymbolOrderByPriceMax(marketSymbol));
    }

    @GetMapping
    public List<CryptocurrencyResponseDto> getAll(@RequestParam(defaultValue = "name")
                                                          String name,
                                                  @RequestParam(defaultValue = "10")
                                                          Integer size,
                                                  @RequestParam(defaultValue = "0")
                                                          Integer page,
                                                  @RequestParam(defaultValue = "price")
                                                          String sortBy) {
        Sort sort = Sort.by(sortCryptocurrencyUtil.getSortingCryptocurrency(sortBy));
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return cryptocurrencyService.findAllByMarketSymbolOrderByPriceAsc(name,
                        pageRequest).stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/csv")
    public String writePricesToFile() {
        writerService.writePricesToFile(PATH_REPORT);
        return "The report was created, see: src/main/resources/";
    }
}

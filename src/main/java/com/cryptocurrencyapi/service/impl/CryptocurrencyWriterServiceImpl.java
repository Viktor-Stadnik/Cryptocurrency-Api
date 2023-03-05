package com.cryptocurrencyapi.service.impl;

import com.cryptocurrencyapi.service.CryptocurrencyReportService;
import com.cryptocurrencyapi.service.CryptocurrencyWriterService;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class CryptocurrencyWriterServiceImpl implements CryptocurrencyWriterService {
    private final CryptocurrencyReportService reportService;

    public CryptocurrencyWriterServiceImpl(CryptocurrencyReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void writePricesToFile(String path) {
        String[] header = {"Cryptocurrency Name", "Min Price", "Max Price"};
        String[] btc = reportService.getCryptocurrencyReport("BTC");
        String[] eth = reportService.getCryptocurrencyReport("ETH");
        String[] xrp = reportService.getCryptocurrencyReport("XRP");
        File file = new File(path);
        try (FileWriter outputFile = new FileWriter(file);
                CSVWriter writer = new CSVWriter(outputFile)) {
            writer.writeNext(header);
            writer.writeNext(btc);
            writer.writeNext(eth);
            writer.writeNext(xrp);
        } catch (IOException e) {
            throw new RuntimeException("Can't write info to the file: " + path, e);
        }
    }
}

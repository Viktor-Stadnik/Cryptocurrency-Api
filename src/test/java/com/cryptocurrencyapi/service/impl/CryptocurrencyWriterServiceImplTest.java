package com.cryptocurrencyapi.service.impl;

import com.cryptocurrencyapi.service.CryptocurrencyReportService;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CryptocurrencyWriterServiceImplTest {
    private static final String TEST_PATH_REPORT = "src/test/resources/testreport.csv";
    private static final String EMPTY_PATH = " ";

    @InjectMocks
    private CryptocurrencyWriterServiceImpl writerService;

    @Mock
    private CryptocurrencyReportService reportService;

    @BeforeEach
    void setUp() {
        Mockito.when(reportService.getCryptocurrencyReport("BTC"))
                .thenReturn(new String[]{"BTC", "1000", "1500"});
        Mockito.when(reportService.getCryptocurrencyReport("ETH"))
                .thenReturn(new String[]{"ETH", "800", "1300"});
        Mockito.when(reportService.getCryptocurrencyReport("XRP"))
                .thenReturn(new String[]{"XRP", "0.2", "0.8"});
    }

    @Test
    public void getCsvFileWithReport() {
        writerService.writePricesToFile(TEST_PATH_REPORT);

        List<String> expect = List.of(
                "Cryptocurrency Name", "Min Price", "Max Price" + ", "
                        + "BTC", "1000", "1500" + ", "
                        + "ETH", "800", "1300" + ", "
                        + "XRP", "0.2", "0.8");
        List<String[]> listReport = null;

        try (FileReader file = new FileReader(TEST_PATH_REPORT);
                CSVReader reader = new CSVReader(file)) {
            listReport = reader.readAll();
        } catch (IOException e) {
            throw new RuntimeException("Can't write info to the file: "
                    + TEST_PATH_REPORT, e);
        }

        List<String> actual = listReport.stream()
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        Assertions.assertEquals(expect.toString(), actual.toString());
    }

    @Test
    public void writePricesToFile_Runtime() {
        try {
            writerService.writePricesToFile(EMPTY_PATH);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Can't write info to the file: "
                    + EMPTY_PATH, e.getMessage());
            return;
        }
        Assertions.fail("Expected receive RuntimeException");
    }
}

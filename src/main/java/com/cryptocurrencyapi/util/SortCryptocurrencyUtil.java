package com.cryptocurrencyapi.util;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class SortCryptocurrencyUtil {
    private static final int FIELD_INDEX = 0;
    private static final int DIRECTION_INDEX = 1;

    public Sort.Order getSortingCryptocurrency(String sortParameter) {
        Sort.Order order;
        if (sortParameter.contains(":")) {
            String[] directional = sortParameter.split(":");
            order = new Sort.Order(Sort.Direction.valueOf(directional[DIRECTION_INDEX]),
                    directional[FIELD_INDEX]);
        } else {
            order = new Sort.Order(Sort.Direction.ASC, sortParameter);
        }
        return order;
    }
}

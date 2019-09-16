package com.murray.financial.querybuilder;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Column result ordering condition
 */
public enum OrderBy {

    ASC("asc"),
    DESC("desc");


    private String sign;

    OrderBy(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public static  Optional<OrderBy> findBy(final String sign){

        return Stream.of(OrderBy.values())
                .filter(fl->fl.sign.equalsIgnoreCase(sign))
                .findFirst();

    }
}

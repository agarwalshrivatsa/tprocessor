package com.pmspod.enums;

public enum OrderType {
    BUY("BUY"),
    SELL("SELL");

    private String value;

    OrderType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

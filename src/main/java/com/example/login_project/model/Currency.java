package com.example.login_project.model;

public class Currency {

    String currencyName;
    Double exchangeRate;

    public Currency() {
    }

    public Currency(String currencyName, Double exchangeRate) {
        this.currencyName = currencyName;
        this.exchangeRate = exchangeRate;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }
}

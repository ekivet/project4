package com.ek.erickivet.stockdata;

/**
 * Created by erickivet on 9/12/16.
 */
public class Stock {
    private String name;
    private String symbol;
    private String exchange;
    private double price;
    private double change;
    private double percent;
    private String timestamp;
    private double quantity;



    public Stock(String name, String symbol, String exchange, double price,
                 double change, double percent, String timestamp,
                 double quantity) {
        this.name = name;
        this.symbol = symbol;
        this.exchange = exchange;
        this.price = price;
        this.change = change;
        this.percent = percent;
        this.timestamp = timestamp;
        this.quantity = quantity;
    }

    public String getStockName() {
        return name;
    }

    public void seStockName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }


}

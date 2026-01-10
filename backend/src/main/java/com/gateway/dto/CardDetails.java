package com.gateway.dto;

public class CardDetails {

    private String number;
    private String expiry_month;
    private String expiry_year;
    private String cvv;
    private String holder_name;

    public String getNumber() { return number; }
    public String getExpiry_month() { return expiry_month; }
    public String getExpiry_year() { return expiry_year; }
    public String getCvv() { return cvv; }
    public String getHolder_name() { return holder_name; }
}

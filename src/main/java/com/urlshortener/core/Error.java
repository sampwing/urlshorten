package com.urlshortener.core;

public class Error {
    private String error;

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public Error(String error) {
        this.error = error;
    }
}

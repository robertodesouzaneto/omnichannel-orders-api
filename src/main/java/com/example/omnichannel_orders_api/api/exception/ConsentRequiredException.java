package com.example.omnichannel_orders_api.api.exception;

public class ConsentRequiredException extends RuntimeException {
    public ConsentRequiredException() {
        super("You must accept the data processing terms before placing an order. Please visit /consent to review and accept.");
    }
}

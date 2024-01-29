package com.example.customer.controller;

public class MessagingException extends Exception {
    public MessagingException() {
        super("Messaging error occurred.");
    }
}

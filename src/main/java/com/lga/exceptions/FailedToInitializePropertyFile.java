package com.lga.exceptions;

public class FailedToInitializePropertyFile extends RuntimeException{
    public FailedToInitializePropertyFile(String message){
        super(message);
    }
}

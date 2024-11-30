package com.nhnacademy.minidooray3teamaccountapi.exception;

public class MemberAlreadyExistsException extends RuntimeException {
    public MemberAlreadyExistsException(String message) {
        super(message);

    }
}
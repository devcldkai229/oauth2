package com.example.demo.exception;


public class TokenExpiredTimeException extends RuntimeException{

    public TokenExpiredTimeException(String msg){
        super(msg);
    }
}

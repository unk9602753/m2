package com.epam.esm.exception;

import lombok.Getter;

public class ServiceException extends Exception{

    private long id;
    public ServiceException(String message){
        super(message);
    }

    public ServiceException(String message, long id){
        super(message);
        this.id = id;
    }

    public String getId(){
        if(id!=0){
            return String.valueOf(id);
        }
        return "";
    }
}

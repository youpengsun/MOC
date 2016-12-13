package com.sap.moc.exception;

public class MOCException extends RuntimeException{
    private static final long serialVersionUID = 4791626562621037838L;
    private long errorCode;
    private String errorMessage;
    
    public MOCException(long errorCode,String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public MOCException(long errorCode){
        this(errorCode,null);
    }

    @Override
    public String toString() {
        return "{\"errorCode\":\""+errorCode+"\",\"errorMessage\":\""+errorMessage+"\"}";
    }
}

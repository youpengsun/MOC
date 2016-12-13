package com.sap.moc.exception;

public class WeChatException extends MOCException{

    private static final long serialVersionUID = -2997284630515694545L;
    
    private String openID;
    
    private String fromID;

    public WeChatException(long errorCode) {
        super(errorCode);
    }

    public WeChatException(long errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

}

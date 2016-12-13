package com.sap.moc.exception;


public class AdminException extends MOCException {

    private static final long serialVersionUID = 8921327694148487680L;
    public AdminException(long errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
    public AdminException(long errorCode) {
        super(errorCode);
    }

}

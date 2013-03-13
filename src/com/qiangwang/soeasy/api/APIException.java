package com.qiangwang.soeasy.api;

public class APIException extends Exception {

    private static final long serialVersionUID = 475022994858770424L;

    private String errorCode;
    private String errorMsg;
    private int httpCode;
    private String errorURI;

    public APIException(String errorMsg) {
        this(errorMsg, null);
    }

    public APIException(String errorMsg, String errorCode) {
        this(errorMsg, errorCode, 200);
    }

    public APIException(String errorMsg, String errorCode, int httpCode) {
        this(errorMsg, errorCode, httpCode, null);
    }

    public APIException(String errorMsg, String errorCode, int httpCode,
            String errorURI) {
        super(errorMsg);

        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
        this.httpCode = httpCode;
        this.setErrorURI(errorURI);
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getErrorURI() {
        return errorURI;
    }

    public void setErrorURI(String errorURI) {
        this.errorURI = errorURI;
    }

    public String toString() {
        return "errorCode:" + errorCode + "\nerrorMsg:" + errorMsg;
    }

}

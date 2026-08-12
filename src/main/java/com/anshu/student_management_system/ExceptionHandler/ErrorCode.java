package com.anshu.student_management_system.ExceptionHandler;

public enum ErrorCode implements ErrorHandle {

    ERR_AP_2000(2000,"Username already Present"),
    ERR_AP_2001(2001,"Refresh Token Expired or Invalid")
    ;

    private final Integer errCode;

    private final String message;

    ErrorCode(Integer errCode,String message){
        this.errCode = errCode;
        this.message = message;
    }

    @Override
    public Integer getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

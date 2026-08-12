package com.anshu.student_management_system.ExceptionHandler;

public enum ErrorCode implements ErrorHandle {

    ERR_AP_2000(2000,"Username already Present"),
    ERR_AP_2001(2001,"Refresh Token Expired or Invalid"),
    ERR_AP_2002(2002,"Student Code already exists"),
    ERR_AP_2003(2003,"Student not found"),
    ERR_AP_2004(2004,"Course not found"),
    ERR_AP_2005(2005,"Student already enrolled in the course"),
    ERR_AP_2006(2006,"Student not enrolled in the course"),
    ERR_AP_2007(2007,"Student Code is required"),
    ERR_AP_2008(2008,"Date of birth is required"),
    ERR_AP_2009(2009,"Invalid date of birth format. Expected yyyy-MM-dd"),
    ERR_AP_2010(2010,"Invalid student code or date of birth"),
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

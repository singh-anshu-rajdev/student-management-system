package com.anshu.student_management_system.ExceptionHandler;

public class CustomValidationException extends IllegalArgumentException{

    /**
     * The ErrorCode
     */
    private final ErrorCode errorCode;

    /**
     *
     * @param errorCode
     */
    public CustomValidationException(ErrorCode errorCode) {
        super(getMessage(errorCode));
        this.errorCode = errorCode;
    }

    /**
     *
     * @return ErrorCode
     */
    public ErrorCode getErrorCode(){
        return errorCode;
    }

    /**
     *
     * @param errorCode
     * @return
     */
    private static String getMessage(ErrorCode errorCode){
        if(null!=errorCode.getMessage()){
            return errorCode.getMessage();
        }else{
            return null;
        }
    }
}

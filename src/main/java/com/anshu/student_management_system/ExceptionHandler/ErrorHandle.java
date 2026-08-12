package com.anshu.student_management_system.ExceptionHandler;

public interface ErrorHandle {

    /**
     *
     * @return ErrorCode
     */
    Integer getErrorCode();

    /**
     *
     * @return Message
     */
    String getMessage();
}

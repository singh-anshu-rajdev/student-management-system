package com.anshu.student_management_system.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorMessage {

    /**
     * The ErrorCode
     */
    private Integer errorCode;

    /**
     * The Message
     */
    private String message;


}

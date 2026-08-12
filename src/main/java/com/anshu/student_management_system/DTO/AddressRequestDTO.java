package com.anshu.student_management_system.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressRequestDTO {

    private Long id;
    private String addressType;
    private String addressLine;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}

package com.reliaquest.api.model;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateEmployeeInput {
    private String name;
    private Integer salary;
    private Integer age;
    private String title;
}

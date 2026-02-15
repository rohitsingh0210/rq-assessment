package com.reliaquest.api.model;

import lombok.Getter;

import java.util.List;

public class Response {
    @Getter
    List<Employee> data;
    @Getter
    String status;

}

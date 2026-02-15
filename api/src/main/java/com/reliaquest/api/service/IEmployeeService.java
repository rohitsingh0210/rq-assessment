package com.reliaquest.api.service;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.Employee;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IEmployeeService {
    List<Employee> getAllEmployees();
    List<Employee> getEmployeesByNameSearch(@PathVariable String searchString);
    Employee getEmployeeById(@PathVariable String id);
    int getHighestSalaryOfEmployees();
    List<String> getTopTenHighestEarningEmployeeNames();
    Employee createEmployee(@RequestBody CreateEmployeeInput employeeInput);
    String deleteEmployeeById(@PathVariable String id);
}

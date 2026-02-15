package com.reliaquest.api.service;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.DeleteEmployeeInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.web.EmployeeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeService implements IEmployeeService {

    private final EmployeeClient employeeClient;

    public EmployeeService(EmployeeClient employeeClient) {
        this.employeeClient = employeeClient;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeClient.getAllEmployees();
    }

    @Override
    public int getHighestSalaryOfEmployees() {
        List<Employee> employees = employeeClient.getAllEmployees();

        if (employees.isEmpty()) {
            log.warn("Employee list is empty");
            return 0;
        }
        return employees.stream()
                .map(Employee::getSalary)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0);
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        List<Employee> employees = employeeClient.getAllEmployees();

        if (employees.isEmpty()) {
            log.warn("Employee list is empty");
            return new ArrayList<>();
        }
        return employees.stream()
                .filter(Objects::nonNull)
                .filter(employee -> employee.getName() != null && employee.getName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());
    }
    @Override
    public Employee getEmployeeById(String id) {
        return employeeClient.getEmployeeById(id);
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = employeeClient.getAllEmployees();

        if (employees.isEmpty()) {
            log.warn("Employee list is empty");
            return new ArrayList<>();
        }
        PriorityQueue<Employee> pq = new PriorityQueue<>(10, (a,b)->a.getSalary() - b.getSalary());
        for(Employee employee:employees){
            if(employee == null)
                continue;
            pq.offer(employee);
            while(pq.size() > 10){
                pq.poll();
            }
        }

        return pq.stream()
                .sorted((a,b)->b.getSalary()-a.getSalary())
                .limit(10)
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Employee createEmployee(CreateEmployeeInput employeeInput) {
        if(employeeInput == null)
            throw new IllegalArgumentException("Input employee is null");
        return employeeClient.createEmployee(employeeInput);
    }

    @Override
    public String deleteEmployeeById(String id) {
        if(id == null || id.isEmpty()){
            throw new IllegalArgumentException("Employee is null or empty");
        }
        Employee employee = employeeClient.getEmployeeById(id);
        if(employee == null)
            throw new RuntimeException("Employee not found with id "+id);

        boolean isDeleted = employeeClient.deleteEmployeeByName(employee.getName());
        if(!isDeleted)
            throw new RuntimeException("Delete operation failed for id "+id);
        return employee.getName();
    }

}

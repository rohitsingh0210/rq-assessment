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
        log.debug("EmployeeService | Fetching all employees...");
        List<Employee> employees = employeeClient.getAllEmployees();
        if(employees != null && !employees.isEmpty())
            log.debug("EmployeeService | Fetched all employees successfully...");
        else log.warn("EmployeeService | No employees found...");
        return employees;
    }

    @Override
    public int getHighestSalaryOfEmployees() {
        List<Employee> employees = getAllEmployees();

        if (employees.isEmpty()) {
            log.error("Employee list is empty");
            return 0;
        }
        log.info("EmployeeService | Fetching highest salary...");
        int salary = employees.stream()
                .map(employee -> employee.getSalary())
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0);
        if(salary != 0)
            log.info("EmployeeService | Highest salary is "+salary);
        return salary;
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        if(searchString == null || searchString.isEmpty()){
            log.error("EmployeeService | Invalid searchString "+searchString);
            throw new IllegalArgumentException("searchString is null/empty");
        }
        List<Employee> employees = getAllEmployees();

        if (employees.isEmpty()) {
            log.warn("Employee list is empty");
            return new ArrayList<>();
        }
        log.info("EmployeeService | Searching for employee with searchString "+searchString);
        List<Employee> result = employees.stream()
                .filter(Objects::nonNull)
                .filter(employee ->
                        employee.getName() != null
                        && employee.getName().toLowerCase().contains(searchString.toLowerCase())
                )
                .collect(Collectors.toList());
        if(!result.isEmpty())
            log.info(String.format("EmployeeService | %d matching employees found...", result.size()));
        return result;
    }
    @Override
    public Employee getEmployeeById(String id) {
        if(id == null || id.isEmpty()){
            log.error("EmployeeService | Invalid id "+id);
            throw new IllegalArgumentException("Id is null");
        }
        log.info("EmployeeService | Fetching employee with id "+id);
        Employee employee = employeeClient.getEmployeeById(id);
        if(employee != null)
            log.info("EmployeeService | Found employee "+employee);
        return employee;
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = getAllEmployees();

        if (employees.isEmpty()) {
            log.warn("Employee list is empty");
            return new ArrayList<>();
        }

        log.info("EmployeeService | calculating top 10 salaries...");
        PriorityQueue<Employee> pq = new PriorityQueue<>(10, (a,b)->a.getSalary() - b.getSalary());
        for(Employee employee:employees){
            if(employee == null)
                continue;
            pq.offer(employee);
            while(pq.size() > 10){
                pq.poll();
            }
        }

        List<String> result = pq.stream()
                .sorted((a,b)->b.getSalary()-a.getSalary())
                .limit(10)
                .map(Employee::getName)
                .collect(Collectors.toList());
        if(!result.isEmpty())
            log.info(String.format("EmployeeService | Highest paid employees found..."));
        return result;
    }

    @Override
    public Employee createEmployee(CreateEmployeeInput employeeInput) {
        log.info("EmployeeService | Creating employee "+employeeInput);
        if(employeeInput == null)
            throw new IllegalArgumentException("Input employee is null");
        Employee employee = employeeClient.createEmployee(employeeInput);
        if(employee != null)
            log.info(String.format("EmployeeService | Employee created ", employee));
        return employee;
    }

    @Override
    public String deleteEmployeeById(String id) {
        if(id == null || id.isEmpty()){
            log.error("EmployeeService | Invalid id "+id);
            throw new IllegalArgumentException("Id is null or empty");
        }
        Employee employee = getEmployeeById(id);
        if(employee == null)
            throw new RuntimeException("Employee not found with id "+id);

        String name = employee.getName();
        log.info(String.format("EmployeeService | Deleting employee with id %s name %s", id, name));
        boolean isDeleted = employeeClient.deleteEmployeeByName(name);
        if(!isDeleted) {
            throw new RuntimeException("Delete operation failed for id " + id);
        }
        else log.info(String.format("EmployeeService | Deleted employee with id %s name %s", id, name));
        return name;
    }

}

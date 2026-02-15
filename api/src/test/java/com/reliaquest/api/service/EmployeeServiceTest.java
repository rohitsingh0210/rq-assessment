package com.reliaquest.api.service;

import com.reliaquest.api.exception.MockServerException;
import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.web.EmployeeClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeClient employeeClient;
    @InjectMocks
    private EmployeeService employeeService;

    // fetch all employees
    @Test
    void fetchAllEmployees(){
        List<Employee> employees = List.of(
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Rohit", 10000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Rohan", 8000, 30, "Dev", "b@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Reliaquest", 9000, 28, "Dev", "c@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000004"), "Someone", 7000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000005"), "Nobody", 6000, 30, "Dev", "b@x.com")
        );
        when(employeeClient.getAllEmployees()).thenReturn(employees);
        List<Employee> res = employeeService.getAllEmployees();
        assertEquals(employees, res);
    }

    // fetch employee by name search tests
    @Test
    void shouldFetchEmployeeByNamePattern(){
        List<Employee> employees = List.of(
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Rohit", 10000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Rohan", 8000, 30, "Dev", "b@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Reliaquest", 9000, 28, "Dev", "c@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000004"), "Someone", 7000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000005"), "Nobody", 6000, 30, "Dev", "b@x.com")
        );
        when(employeeClient.getAllEmployees()).thenReturn(employees);
        List<Employee> res = employeeService.getEmployeesByNameSearch("roh");
        List<Employee> list = List.of(
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Rohit", 10000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Rohan", 8000, 30, "Dev", "b@x.com")
        );
        assertEquals(res, list);
    }
    @Test
    void shouldNotFetchEmployeeByNamePatternWhenAbsent(){
        List<Employee> employees = List.of(
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Rohit", 10000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Rohan", 8000, 30, "Dev", "b@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Reliaquest", 9000, 28, "Dev", "c@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000004"), "Someone", 7000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000005"), "Nobody", 6000, 30, "Dev", "b@x.com")
        );
        when(employeeClient.getAllEmployees()).thenReturn(employees);
        List<Employee> res = employeeService.getEmployeesByNameSearch("xyz");
        List<Employee> list = Collections.emptyList();
        assertEquals(res, list);
    }


    // fetch by id tests
    @Test
    void shouldFetchEmployeeById(){

        Employee employee = new Employee(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Rohit", 10000, 25, "Dev", "a@x.com");
        when(employeeClient.getEmployeeById("00000000-0000-0000-0000-000000000001")).thenReturn(employee);

        Employee result = employeeService.getEmployeeById("00000000-0000-0000-0000-000000000001");
        assertEquals(employee, result);
    }
    @Test
    void shouldThrowWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                employeeService.getEmployeeById(null)
        );
    }
    @Test
    void shouldThrowWhenEmployeeNotFound() {

        when(employeeClient.getEmployeeById("00000000-0000-0000-0000-000000000001"))
                .thenThrow(new MockServerException("Not found"));

        assertThrows(MockServerException.class, () ->
                employeeService.getEmployeeById("00000000-0000-0000-0000-000000000001")
        );
    }

    // highest salary tests
    @Test
    void shouldReturnHighestSalariedEmployee(){
        List<Employee> employees = List.of(
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Rohit", 10000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Rohan", 8000, 30, "Dev", "b@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Reliaquest", 9000, 28, "Dev", "c@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000004"), "Someone", 7000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000005"), "Nobody", 6000, 30, "Dev", "b@x.com")
        );
        when(employeeClient.getAllEmployees()).thenReturn(employees);
        int salary = employeeService.getHighestSalaryOfEmployees();
        assertEquals(salary, 10000);
    }
    @Test
    void shouldReturn0HighestSalariedEmployee_WhenNoEmployee(){
        List<Employee> employees = Collections.emptyList();
        when(employeeClient.getAllEmployees()).thenReturn(employees);
        int salary = employeeService.getHighestSalaryOfEmployees();
        assertEquals(salary, 0);
    }

    // top 10 salaries tests
    @Test
    void shouldReturnTop10HighestEarningEmployees() {
        List<Employee> employees = List.of(
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000001"), "A", 10000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000002"), "B", 8000, 30, "Dev", "b@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000003"), "C", 9000, 28, "Dev", "c@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000004"), "D", 7000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000005"), "E", 6000, 30, "Dev", "b@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000006"), "F", 5000, 28, "Dev", "c@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000007"), "G", 4000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000008"), "H", 3000, 30, "Dev", "b@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000009"), "I", 2000, 28, "Dev", "c@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000010"), "J", 1000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000011"), "K", 500, 25, "Dev", "a@x.com")
        );
        when(employeeClient.getAllEmployees()).thenReturn(employees);
        List<String> result = employeeService.getTopTenHighestEarningEmployeeNames();
        assertEquals(List.of("A", "C","B","D","E","F","G","H","I","J"), result);
    }
    @Test
    void shouldReturnEmptyListWhenNoEmployees() {
        List<Employee> employees = new ArrayList<>();
        when(employeeClient.getAllEmployees()).thenReturn(employees);
        List<String> result = employeeService.getTopTenHighestEarningEmployeeNames();
        assertTrue(result.isEmpty());
    }
    @Test
    void shouldReturnTop10HighestEarningEmployees_WithLessThan10Employees() {
        List<Employee> employees = List.of(
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000001"), "A", 10000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000002"), "B", 8000, 30, "Dev", "b@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000003"), "C", 9000, 28, "Dev", "c@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000004"), "D", 7000, 25, "Dev", "a@x.com"),
                new Employee(UUID.fromString("00000000-0000-0000-0000-000000000005"), "E", 6000, 30, "Dev", "b@x.com")
        );
        when(employeeClient.getAllEmployees()).thenReturn(employees);
        List<String> result = employeeService.getTopTenHighestEarningEmployeeNames();
        assertEquals(List.of("A", "C","B","D","E"), result);
    }

    // create employee tests
    @Test
    void shouldCreateEmployee() {

        CreateEmployeeInput input = new CreateEmployeeInput("Rohit", 5000, 30, "Engineer");
        Employee employee = new Employee(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Rohit", 5000, 30, "Engineer", "j@x.com");

        when(employeeClient.createEmployee(input)).thenReturn(employee);
        Employee result = employeeService.createEmployee(input);
        assertEquals("Rohit", result.getName());
    }
    @Test
    void shouldThrowWhenCreateInputIsNull() {
        assertThrows(IllegalArgumentException.class, () ->employeeService.createEmployee(null));
    }
    @Test
    void shouldThrowWhenDownstreamReturnsBadRequest() {
        CreateEmployeeInput input = new CreateEmployeeInput();
        when(employeeClient.createEmployee(input)).thenThrow(new MockServerException("Invalid"));
        assertThrows(MockServerException.class, () -> employeeService.createEmployee(input));
    }

    // delete employee tests
    @Test
    void shouldDeleteEmployeeById() {
        Employee employee = new Employee(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Rohit", 2000, 30, "Dev", "j@x.com");
        when(employeeClient.getEmployeeById("00000000-0000-0000-0000-000000000001")).thenReturn(employee);
        when(employeeClient.deleteEmployeeByName("Rohit")).thenReturn(true);

        String result = employeeService.deleteEmployeeById("00000000-0000-0000-0000-000000000001");
        assertEquals("Rohit", result);
    }
    @Test
    void deleteEmployeeById_withWrongId() {
        Employee employee = new Employee(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Rohit", 2000, 30, "Dev", "j@x.com");
        when(employeeClient.getEmployeeById("00000000-0000-0000-0000-000000000001")).thenReturn(employee);
        when(employeeClient.deleteEmployeeByName("Rohit")).thenReturn(true);

        String result = employeeService.deleteEmployeeById("00000000-0000-0000-0000-000000000001");
        assertEquals("Rohit", result);
    }



}
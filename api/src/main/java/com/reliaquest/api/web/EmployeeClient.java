package com.reliaquest.api.web;

import com.reliaquest.api.exception.EmployeeNotFoundException;
import com.reliaquest.api.exception.MockServerException;
import com.reliaquest.api.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class EmployeeClient {

    private final RestClient restClient;

    public EmployeeClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Employee> getAllEmployees() {
        try {
            log.debug("EmployeeClient | Calling Mock employee API");
            Response response = restClient
                    .get()
                    .uri("/api/v1/employee")
                    .retrieve()
                    .body(Response.class);

            return Optional.ofNullable(response)
                    .map(response1 -> response1.getData())
                    .orElse(Collections.emptyList());
        }
        catch (HttpClientErrorException.NotFound e) {
            log.error("EmployeeClient | Employees not found "+ e.getMessage());
            throw new EmployeeNotFoundException("Employees not found ", e);
        } catch (ResourceAccessException e) {
            log.error("EmployeeClient | Mock API unreachable "+e.getMessage());
            throw new MockServerException("Mock API unreachable ", e);
        }catch (Exception e) {
            log.error("EmployeeClient | Mock API error", e.getMessage());
            throw new MockServerException("Failed to fetch employees", e);
        }
    }

    public Employee getEmployeeById(String id) {
        try {
            log.debug("EmployeeClient | Calling Mock employee API");
            EmployeeResponse response = restClient
                    .get()
                    .uri("/api/v1/employee/{id}", id)
                    .retrieve()
                    .body(EmployeeResponse.class);

            return Optional.ofNullable(response)
                    .map(employeeResponse -> employeeResponse.getData())
                    .orElse(null);

        }
        catch (HttpClientErrorException.NotFound e) {
            log.error("EmployeeClient | Employee not found with id: {}", id);
            throw new EmployeeNotFoundException("Employee not found with id: " + id, e);
        } catch (ResourceAccessException e) {
            log.error("EmployeeClient | Mock API unreachable "+e.getMessage());
            throw new MockServerException("Mock API unreachable ", e);
        }catch (Exception e) {
            log.error("EmployeeClient | Mock API error", e.getMessage());
            throw new MockServerException("Failed to fetch employees", e);
        }
    }

    public Employee createEmployee(CreateEmployeeInput employeeInput) {

        try {
            log.debug("EmployeeClient | Calling Mock employee API");
            EmployeeResponse response = restClient
                    .post()
                    .uri("/api/v1/employee")
                    .body(employeeInput)
                    .retrieve()
                    .body(EmployeeResponse.class);

            if (response == null || response.getData() == null) {
                throw new MockServerException("Failed to create employee");
            }

            return response.getData();

        } catch (HttpClientErrorException.BadRequest e) {
            log.error("EmployeeClient | Invalid employee data sent to mock server "+ e.getMessage());
            throw new MockServerException("Invalid employee data sent to mock server ", e);
        } catch (ResourceAccessException e) {
            log.error("EmployeeClient | Mock API unreachable "+e.getMessage());
            throw new MockServerException("Mock API unreachable ", e);
        } catch (Exception e) {
            log.error("EmployeeClient | Employee creation failed ... Check employee details constraints", e.getMessage());
            throw new MockServerException("Check employee details constraints", e);
        }
    }

    public Boolean deleteEmployeeByName(String name) {

        try {
            log.debug("EmployeeClient | Calling Mock employee API");
            DeleteEmployeeResponse response = restClient
                    .method(HttpMethod.DELETE)
                    .uri("/api/v1/employee")
                    .body(new DeleteEmployeeInput(name))
                    .retrieve()
                    .body(DeleteEmployeeResponse.class);

            if (response == null || response.getData() == null) {
                throw new MockServerException("Failed to delete employee");
            }

            return response.getData();

        } catch (HttpClientErrorException.NotFound e) {
            log.error("EmployeeClient | Employee not found with name: {}", name);
            throw new EmployeeNotFoundException("Employee not found with name: " + name, e);
        } catch (ResourceAccessException e) {
            log.error("EmployeeClient | Mock API unreachable "+e.getMessage());
            throw new MockServerException("Mock API unreachable ", e);
        } catch (Exception e) {
            log.error("EmployeeClient | Error deleting employee ", e.getMessage());
            throw new MockServerException("EmployeeClient | Error deleting employee ", e);
        }
    }

}

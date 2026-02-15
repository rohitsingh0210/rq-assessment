package com.reliaquest.api.web;

import com.reliaquest.api.exception.MockServerException;
import com.reliaquest.api.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
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
            log.info("Calling Mock employee API");
            Response response = restClient
                    .get()
                    .uri("/api/v1/employee")
                    .retrieve()
                    .body(Response.class);

            return Optional.ofNullable(response)
                    .map(Response::getData)
                    .orElse(Collections.emptyList());

        } catch (Exception ex) {
            log.error("Mock API error", ex.getMessage());
            throw new MockServerException("Failed to fetch employees", ex);
        }
    }

    public Employee getEmployeeById(String id) {
        try {
            log.debug("Calling Mock employee API");
            EmployeeResponse response = restClient
                    .get()
                    .uri("/api/v1/employee/{id}", id)
                    .retrieve()
                    .body(EmployeeResponse.class);

            return Optional.ofNullable(response)
                    .map(EmployeeResponse::getData)
                    .orElse(null);

        }
        catch (HttpClientErrorException.NotFound ex) {
            log.warn("Employee not found in downstream for id: {}", id);
            throw new MockServerException("Employee not found with id: " + id);
        } catch (Exception ex) {
            log.error("Mock API error", ex);
            throw new MockServerException("Failed to fetch employees", ex);
        }
    }

    public Employee createEmployee(CreateEmployeeInput employeeInput) {

        try {
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

        } catch (Exception ex) {
            log.error("Error creating employee", ex);
            throw new MockServerException("Downstream create failed", ex);
        }
    }

    public Boolean deleteEmployeeByName(String name) {

        try {
            DeleteEmployeeResponse response = restClient
                    .method(HttpMethod.DELETE)
                    .uri("/api/v1/employee")
                    .body(new DeleteEmployeeInput(name))
                    .retrieve()
                    .body(DeleteEmployeeResponse.class);

            if (response == null || response.getData() == null) {
                throw new MockServerException("Failed to create employee");
            }

            return response.getData();

        } catch (Exception ex) {
            log.error("Error creating employee", ex);
            throw new MockServerException("Downstream create failed", ex);
        }
    }

}

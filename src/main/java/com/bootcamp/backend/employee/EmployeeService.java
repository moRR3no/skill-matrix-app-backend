package com.bootcamp.backend.employee;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(UUID id) {
        Optional<Employee> result = employeeRepository.findById(id);
        Employee employee = null;
        if(result.isPresent()) {
            employee=result.get();
        } else {
            throw new RuntimeException("Did not found employee with id=" + id);
        }
        return employee;
    }

    public Employee saveEmployee (Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteById (UUID id) {
        employeeRepository.deleteById(id);
    }


}

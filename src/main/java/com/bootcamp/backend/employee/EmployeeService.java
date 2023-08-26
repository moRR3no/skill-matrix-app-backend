package com.bootcamp.backend.employee;

import com.bootcamp.backend.exceptions.NotFoundException;
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
        Optional<Employee> employeeResult = employeeRepository.findById(id);
        return employeeResult.orElseThrow(() -> new NotFoundException("Employee not found with id=" + id));
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }


    public void deleteById(UUID id) {
        employeeRepository.deleteById(id);
    }
}

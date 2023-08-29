package com.bootcamp.backend.employee;

import com.bootcamp.backend.exceptions.NotFoundException;
import com.bootcamp.backend.exceptions.WrongInputException;
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
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id=" + id));
    }

    public Employee saveEmployee(Employee employee) {
        validateManager(employee);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(UUID pathId, Employee updatedEmployee) {
        UUID employeeId = updatedEmployee.getId();
        if (employeeRepository.existsById(employeeId) && pathId.equals(employeeId)) {
            validateManager(updatedEmployee);
            return employeeRepository.save(updatedEmployee);
        } else {
            throw new WrongInputException("Wrong id input");
        }
    }

    public void deleteById(UUID id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        } else {
            throw new NotFoundException("Employee not found with id=" + id);
        }
    }

    private void validateManager(Employee employee) {
        Employee manager = employee.getManager();
        if (manager != null && !employeeRepository.existsById(manager.getId())) {
            throw new NotFoundException("Manager not found");
        }
    }
}
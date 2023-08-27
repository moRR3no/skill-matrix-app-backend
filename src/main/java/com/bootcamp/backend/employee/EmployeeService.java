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
        Optional<Employee> employeeResult = employeeRepository.findById(id);
        return employeeResult.orElseThrow(() -> new NotFoundException("Employee not found with id=" + id));
    }

    public Employee saveEmployee(Employee employee) throws NotFoundException{
        Employee manager = employee.getManager();
        if (manager == null) {
            return employeeRepository.save(employee);
        } else if (employeeRepository.existsById(manager.getId())) {
            return employeeRepository.save(employee);
        } else {
            throw new NotFoundException("Manager not found");
        }
    }

    public Employee updateEmployee(Employee updatedEmployee) {
        if (employeeRepository.existsById(updatedEmployee.getId())) {
            Employee manager = updatedEmployee.getManager();
            if (manager == null) {
                return employeeRepository.save(updatedEmployee);
            } else if (employeeRepository.existsById(manager.getId())) {
                return employeeRepository.save(updatedEmployee);
            } else {
                throw new NotFoundException("Manager not found");
            }
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
}
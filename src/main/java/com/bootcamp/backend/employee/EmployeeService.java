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
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public List<EmployeeDTO> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.employeesToEmployeeDTOs(employees);
    }

    public EmployeeDTO getEmployeeById(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id=" + id));
        return employeeMapper.employeeToEmployeeDTO(employee);
    }

    public Employee saveEmployee(Employee employee) {
        validateManager(employee);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Employee updatedEmployee) {
        if (!employeeRepository.existsById(updatedEmployee.getId())) {
            throw new WrongInputException("Wrong id input");
        }
        validateManager(updatedEmployee);
        return employeeRepository.save(updatedEmployee);
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
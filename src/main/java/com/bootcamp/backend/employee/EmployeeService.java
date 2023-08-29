package com.bootcamp.backend.employee;

import com.bootcamp.backend.exceptions.NotFoundException;
import com.bootcamp.backend.exceptions.WrongInputException;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
        validateManager(employee);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.employeeToEmployeeDTO(savedEmployee);
    }

    public EmployeeDTO updateEmployee(UUID pathId, EmployeeDTO updatedEmployeeDTO) {
        Employee employee = employeeMapper.employeeDTOToEmployee(updatedEmployeeDTO);
        UUID employeeId = employee.getId();
        if (employeeRepository.existsById(employeeId) && pathId.equals(employeeId)) {
            validateManager(employee);
            employeeRepository.save(employee);
            return employeeMapper.employeeToEmployeeDTO(employee);
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
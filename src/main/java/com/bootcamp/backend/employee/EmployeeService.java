package com.bootcamp.backend.employee;

import com.bootcamp.backend.exceptions.NotFoundException;
import com.bootcamp.backend.exceptions.WrongInputException;
import jakarta.transaction.Transactional;
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

    @Transactional
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
//        validateManager(employee);
        setManagerFromDTO(employeeDTO, employee);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.employeeToEmployeeDTO(savedEmployee);
    }

    @Transactional
    public EmployeeDTO updateEmployee(UUID pathId, EmployeeDTO employeeDTO) {
        UUID employeeId = employeeDTO.getId();
        if (employeeRepository.existsById(employeeId) && pathId.equals(employeeId)) {
            Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
//            validateManager(employee);
            setManagerFromDTO(employeeDTO, employee);
            Employee updatedEmployee = employeeRepository.save(employee);
            return employeeMapper.employeeToEmployeeDTO(updatedEmployee);
        } else {
            throw new WrongInputException("Wrong id input");
        }
    }

    @Transactional
    public void deleteById(UUID id) {
        if (employeeRepository.existsById(id)) {
            setManagerToNull(id);
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

    public Employee getManagerById (UUID managerId) {
        return employeeRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException("Manager not found with id=" + managerId));
    }

    public void setManagerFromDTO (EmployeeDTO employeeDTO, Employee employee) {
        if(employeeDTO.getManagerId() != null) {
            Employee manager = getManagerById(employeeDTO.getManagerId());
            employee.setManager(manager);
        }
    }

    public void setManagerToNull (UUID id) {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee emp : employees) {
            if (emp.getManager() != null && emp.getManager().getId().equals(id)) {
                emp.setManager(null);
            }
        }
    }
}
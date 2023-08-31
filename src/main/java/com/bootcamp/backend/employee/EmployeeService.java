package com.bootcamp.backend.employee;

import com.bootcamp.backend.exceptions.NotFoundException;
import com.bootcamp.backend.exceptions.WrongInputException;
import com.bootcamp.backend.mappers.MapStructMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final MapStructMapper mapstructMapper;

    public EmployeeService(EmployeeRepository employeeRepository, MapStructMapper mapstructMapper) {
        this.employeeRepository = employeeRepository;
        this.mapstructMapper = mapstructMapper;
    }

    public List<EmployeeDTO> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return mapstructMapper.employeesToEmployeeDTOs(employees);
    }

    public EmployeeDTO getEmployeeById(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id=" + id));
        return mapstructMapper.employeeToEmployeeDTO(employee);
    }

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = mapstructMapper.employeeDTOToEmployee(employeeDTO);
        setManagerFromDTO(employeeDTO, employee);
        Employee savedEmployee = employeeRepository.save(employee);
        return mapstructMapper.employeeToEmployeeDTO(savedEmployee);
    }

    public EmployeeDTO updateEmployee(UUID pathId, EmployeeDTO employeeDTO) {
        UUID employeeId = employeeDTO.getId();
        if (employeeRepository.existsById(employeeId) && pathId.equals(employeeId)) {
            Employee employee = mapstructMapper.employeeDTOToEmployee(employeeDTO);
            setManagerFromDTO(employeeDTO, employee);
            Employee updatedEmployee = employeeRepository.save(employee);
            return mapstructMapper.employeeToEmployeeDTO(updatedEmployee);
        } else {
            throw new WrongInputException("Wrong id input");
        }
    }

    public void deleteById(UUID id) {
        if (employeeRepository.existsById(id)) {
            setManagerToNull(id);
            employeeRepository.deleteById(id);
        } else {
            throw new NotFoundException("Employee not found with id=" + id);
        }
    }

    private Employee getManagerById (UUID managerId) {
        return employeeRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException("Manager not found with id=" + managerId));
    }

    private void setManagerFromDTO (EmployeeDTO employeeDTO, Employee employee) {
        if(employeeDTO.getManagerId() != null) {
            Employee manager = getManagerById(employeeDTO.getManagerId());
            employee.setManager(manager);
        }
    }

    private void setManagerToNull (UUID id) {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee emp : employees) {
            if (emp.getManager() != null && emp.getManager().getId().equals(id)) {
                emp.setManager(null);
            }
        }
    }
}
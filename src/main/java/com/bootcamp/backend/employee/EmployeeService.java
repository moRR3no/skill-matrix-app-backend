package com.bootcamp.backend.employee;

import com.bootcamp.backend.auth.AuthResponse;
import com.bootcamp.backend.auth.AuthenticatorRequest;
import com.bootcamp.backend.exceptions.NotFoundException;
import com.bootcamp.backend.exceptions.WrongInputException;
import com.bootcamp.backend.mappers.MapStructMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final MapStructMapper mapstructMapper;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, MapStructMapper mapstructMapper, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.mapstructMapper = mapstructMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<EmployeeDTO> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return mapstructMapper.employeesToEmployeeDTOs(employees);
    }

    public List<EmployeeDTO> getEmployeeByName (String name) {
        List<Employee> employee = employeeRepository.findByFirstName(name);
        return mapstructMapper.employeesToEmployeeDTOs(employee);
    }

    public EmployeeDTO getEmployeeById(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id=" + id));
        return mapstructMapper.employeeToEmployeeDTO(employee);
    }

    public List<EmployeeDTO> getEmployeesByContainingWord (String word) {
        List<Employee> employee = employeeRepository.findByFirstNameOrSurname(word);
        return mapstructMapper.employeesToEmployeeDTOs(employee);
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

    public AuthResponse login (AuthenticatorRequest authenticatorRequest) {
        Optional<Employee> response = this.employeeRepository.findUserByUsername(
                authenticatorRequest.username());
        if (response.isPresent() && passwordEncoder.matches(authenticatorRequest.password(), response.get().getPassword())) {
            return new AuthResponse(authenticatorRequest.username());
        }
        throw new ResponseStatusException(HttpStatusCode.valueOf(401));
    }
}
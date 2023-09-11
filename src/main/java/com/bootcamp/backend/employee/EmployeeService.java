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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public EmployeeDTO getEmployeeById(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id=" + id));
        return mapstructMapper.employeeToEmployeeDTO(employee);
    }

    public List<EmployeeDTO> getEmployeesByContainingWord(String word) {
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

    private Employee getManagerById(UUID managerId) {
        return employeeRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException("Manager not found with id=" + managerId));
    }

    private void setManagerFromDTO(EmployeeDTO employeeDTO, Employee employee) {
        if (employeeDTO.getManagerId() != null) {
            Employee manager = getManagerById(employeeDTO.getManagerId());
            employee.setManager(manager);
        }
    }

    private void setManagerToNull(UUID id) {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee emp : employees) {
            if (emp.getManager() != null && emp.getManager().getId().equals(id)) {
                emp.setManager(null);
            }
        }
    }

    List<Employee> employeesWithMostProjects() {
        int maxProjects = -1;
        List<Employee> employees = employeeRepository.findAll();
        List<Employee> bestEmployees = new ArrayList<>();

        for (Employee employee : employees) {
            int numberOfProjects = employee.getProjects().size();

            if (numberOfProjects > maxProjects) {
                bestEmployees.clear();
                maxProjects = numberOfProjects;
                bestEmployees.add(employee);
            } else if (numberOfProjects == maxProjects) {
                bestEmployees.add(employee);
            }
        }
        return bestEmployees;
    }

    List<Employee> employeesWithMostSkills() {
        if (employeesWithMostProjects().size() == 1) {
            return employeesWithMostProjects();
        } else {
            List<Employee> employees = employeesWithMostProjects();
            int maxSkills = -1;
            List<Employee> bestEmployees = new ArrayList<>();

            for (Employee employee : employees) {
                int numberOfSkills = employee.getSkills().size();

                if (numberOfSkills > maxSkills) {
                    bestEmployees.clear();
                    maxSkills = numberOfSkills;
                    bestEmployees.add(employee);
                } else if (numberOfSkills == maxSkills) {
                    bestEmployees.add(employee);
                }
            }
            return bestEmployees;
        }
    }

    public List<Employee> getEmployeesWithLatestDate() {
        if (employeesWithMostSkills().size() == 1) {
            return employeesWithMostSkills();
        } else {
            List<Employee> allEmployees = employeesWithMostSkills();
            Optional<LocalDate> latestDate = allEmployees.stream()
                    .map(Employee::getDate)
                    .max(LocalDate::compareTo);

            if (latestDate.isPresent()) {
                LocalDate latest = latestDate.get();
                return allEmployees.stream()
                        .filter(employee -> employee.getDate().isEqual(latest))
                        .collect(Collectors.toList());
            } else {
                return List.of();
            }
        }
    }

    public EmployeeDTO getEmployeeOfTheMonth() throws Exception {
        int projectSize = employeesWithMostProjects().size();
        int skillSize = employeesWithMostSkills().size();
        UUID id;

        if (employeesWithMostProjects().size() < 1 && employeesWithMostSkills().size() < 1) {
            throw new Exception("Not enough projects or skills assigned!");
        }

        if (projectSize < 2) {
            id = employeesWithMostProjects().get(0).getId();
        } else if (skillSize < 2) {
            id = employeesWithMostSkills().get(0).getId();
        } else {
            return mapstructMapper.employeeToEmployeeDTO(getEmployeesWithLatestDate().get(0));
        }
        return getEmployeeById(id);
    }


    public AuthResponse login(AuthenticatorRequest authenticatorRequest) {
        Optional<Employee> response = this.employeeRepository.findUserByUsername(
                authenticatorRequest.username());
        if (response.isPresent() && passwordEncoder.matches(authenticatorRequest.password(), response.get().getPassword())) {
            return new AuthResponse(authenticatorRequest.username());
        }
        throw new ResponseStatusException(HttpStatusCode.valueOf(401));
    }
}
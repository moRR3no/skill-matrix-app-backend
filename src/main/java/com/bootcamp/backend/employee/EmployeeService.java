package com.bootcamp.backend.employee;

import com.bootcamp.backend.exceptions.AlreadyExistsException;
import com.bootcamp.backend.exceptions.NotFoundException;
import com.bootcamp.backend.exceptions.WrongInputException;
import com.bootcamp.backend.mappers.MapStructMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public List<EmployeeDTO> getEmployeesByContainingWord(String word) {
        List<Employee> employee = employeeRepository.findByFirstNameOrSurname(word);
        return mapstructMapper.employeesToEmployeeDTOs(employee);
    }

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        UUID id = employeeDTO.getId();
        if (employeeRepository.findById(id).isPresent()) {
            throw new AlreadyExistsException("Employee with id " + id + " already exists");
        }
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

    public List<Employee> employeesWithMostProjects() {
        List<Employee> employees = employeeRepository.findAll();
        return findEmployeesWithMost(employees, Employee::getProjects);
    }

    public List<Employee> employeesWithMostSkills() {
        List<Employee> employees = employeesWithMostProjects();
        return findEmployeesWithMost(employees, Employee::getSkills);
    }

    public List<Employee> getEmployeesWithLatestDate() {
        List<Employee> employees = employeesWithMostSkills();
        if (employees.size() == 1) {
            return employees;
        }
        LocalDate latestDate = findLatestDate(employees);
        return employees.stream()
                .filter(employee -> employee.getDate().isEqual(latestDate))
                .collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeOfTheMonth() {
        List<Employee> projectEmployees = employeesWithMostProjects();
        List<Employee> skillEmployees = employeesWithMostSkills();

        if (projectEmployees.isEmpty() && skillEmployees.isEmpty()) {
            throw new NotFoundException("Not enough projects and skills assigned!");
        }

        if (projectEmployees.size() < 2) {
            return getEmployeeById(projectEmployees.get(0).getId());
        } else if (skillEmployees.size() < 2) {
            return getEmployeeById(skillEmployees.get(0).getId());
        } else {
            return mapstructMapper.employeeToEmployeeDTO(getEmployeesWithLatestDate().get(0));
        }
    }

    private List<Employee> findEmployeesWithMost(List<Employee> employees, Function<Employee, List<?>> propertyExtractor) {
        int maxCount = employees.stream()
                .map(employee -> propertyExtractor.apply(employee).size())
                .max(Integer::compareTo)
                .orElse(-1);

        return employees.stream()
                .filter(employee -> propertyExtractor.apply(employee).size() == maxCount)
                .collect(Collectors.toList());
    }

    private LocalDate findLatestDate(List<Employee> employees) {
        return employees.stream()
                .map(Employee::getDate)
                .max(LocalDate::compareTo)
                .orElseThrow(() -> new NotFoundException("No employees with dates found."));
    }


}
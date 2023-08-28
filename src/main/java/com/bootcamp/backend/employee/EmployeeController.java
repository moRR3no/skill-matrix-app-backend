package com.bootcamp.backend.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getEmployees() {
        return new ResponseEntity<>(employeeService.getEmployees(), HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("employeeId") UUID employeeId) {
        return new ResponseEntity<>(employeeService.getEmployeeById(employeeId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.saveEmployee(employee), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee updatedEmployee) {
        return new ResponseEntity<>(employeeService.updateEmployee(updatedEmployee), HttpStatus.OK);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable UUID employeeId) {
        employeeService.deleteById(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
package com.bootcamp.backend.employee;

import jakarta.validation.Valid;
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
    public ResponseEntity<List<EmployeeDTO>> getEmployees(@RequestParam(required = false) String firstName) {
        if (firstName == null) {
            return new ResponseEntity<>(employeeService.getEmployees(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(employeeService.getEmployeesByContainingWord(firstName), HttpStatus.OK);
        }
    }

    @GetMapping("/employeeOfTheMonth")
    public ResponseEntity<EmployeeDTO> getEmployeeOfTheMonth() {
        return new ResponseEntity<>(employeeService.getEmployeeOfTheMonth(), HttpStatus.OK);
    }


    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("employeeId") UUID employeeId) {
        return new ResponseEntity<>(employeeService.getEmployeeById(employeeId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> addEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return new ResponseEntity<>(employeeService.saveEmployee(employeeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("employeeId") UUID id,
                                                      @Valid @RequestBody EmployeeDTO updatedEmployeeDTO) {
        return new ResponseEntity<>(employeeService.updateEmployee(id, updatedEmployeeDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable UUID employeeId) {
        employeeService.deleteById(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
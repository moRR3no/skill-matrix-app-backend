package com.bootcamp.backend.auth;

import com.bootcamp.backend.employee.EmployeeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
public class AuthenticatorController {

    private final EmployeeService employeeService;

    public AuthenticatorController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public AuthResponse login (@RequestBody AuthenticatorRequest user) {
        return employeeService.login(user);
    }
}

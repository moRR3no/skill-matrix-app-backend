package com.bootcamp.backend.auth;

import com.bootcamp.backend.employee.EmployeeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticatorController {

    private final AuthService authService;

    public AuthenticatorController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login (@RequestBody AuthenticatorRequest user) {
        return authService.login(user);
    }
}

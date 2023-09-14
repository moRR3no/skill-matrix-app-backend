package com.bootcamp.backend.auth;

import com.bootcamp.backend.employee.Employee;
import com.bootcamp.backend.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    EmployeeRepository employeeRepository;
    PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthenticatorRequest authenticatorRequest) {
        Optional<Employee> response = this.employeeRepository.findUserByUsername(
                authenticatorRequest.username());
        if (response.isPresent() && passwordEncoder.matches(authenticatorRequest.password(), response.get().getPassword())) {
            return new AuthResponse(authenticatorRequest.username());
        }
        throw new ResponseStatusException(HttpStatusCode.valueOf(401));
    }
}

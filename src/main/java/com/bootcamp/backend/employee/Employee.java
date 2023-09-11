package com.bootcamp.backend.employee;


import com.bootcamp.backend.project.Project;
import com.bootcamp.backend.skill.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "employee")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Employee implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name")
    @NotBlank
    private String firstName;

    @Column(name = "surname")
    @NotBlank
    private String surname;

    @Column(name = "date")
    @NotNull
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    //TODO: Validation required to skills and projects
    @ManyToMany
    private List<Skill> skills;

    @ManyToMany
    private List<Project> projects;

    private String username;

    private String password;


    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
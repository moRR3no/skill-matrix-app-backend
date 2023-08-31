package com.bootcamp.backend.employee;


import com.bootcamp.backend.project.Project;
import com.bootcamp.backend.skill.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "employee")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Employee {

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

    @ManyToMany
    List<Skill> skills;

    @ManyToMany
    List<Project> projects;
}
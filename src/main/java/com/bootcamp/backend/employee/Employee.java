package com.bootcamp.backend.employee;


import com.bootcamp.backend.project.Project;
import com.bootcamp.backend.skill.Skill;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
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
    @Min(2)
    private String firstName;

    @Column(name = "surname")
    @NotBlank
    @Min(2)
    private String surname;

    @Column(name = "date")
    @NotBlank
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @ManyToMany
    List<Skill> skills;

    @ManyToMany
    List<Project> projects;
}
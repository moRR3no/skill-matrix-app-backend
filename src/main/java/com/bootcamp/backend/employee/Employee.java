package com.bootcamp.backend.employee;


import com.bootcamp.backend.project.Project;
import com.bootcamp.backend.skill.Skill;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    private String firstName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "date")
    private Date date;

    @ManyToMany
    List<Skill> skills;

    @ManyToMany
    List<Project> projects;
}
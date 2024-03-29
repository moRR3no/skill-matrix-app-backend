package com.bootcamp.backend.employee;

import com.bootcamp.backend.project.ProjectDTO;
import com.bootcamp.backend.skill.SkillDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class EmployeeDTO {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("managerId")
    private UUID managerId;

    @JsonProperty("skills")
    List<SkillDTO> skills;

    @JsonProperty("projects")
    List<ProjectDTO> projects;
}
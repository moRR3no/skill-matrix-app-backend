package com.bootcamp.backend.employee;

import com.bootcamp.backend.project.Project;
import com.bootcamp.backend.skill.Skill;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
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
    private Date date;

    @JsonProperty("managerId")
    private UUID managerId;

    @JsonProperty("skills")
    List<Skill> skills;

    @JsonProperty("projects")
    List<Project> projects;
}

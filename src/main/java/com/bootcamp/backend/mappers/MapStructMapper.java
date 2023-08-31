package com.bootcamp.backend.mappers;

import com.bootcamp.backend.employee.Employee;
import com.bootcamp.backend.employee.EmployeeDTO;
import com.bootcamp.backend.project.Project;
import com.bootcamp.backend.project.ProjectDTO;
import com.bootcamp.backend.skill.Skill;
import com.bootcamp.backend.skill.SkillDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MapStructMapper {

    @Mapping(target = "managerId", source = "manager.id")
    EmployeeDTO employeeToEmployeeDTO (Employee employee);
    List<EmployeeDTO> employeesToEmployeeDTOs (List<Employee> employees);
    Employee employeeDTOToEmployee (EmployeeDTO employeeDTO);

    ProjectDTO projectToProjectDTO(Project project);
    List<ProjectDTO> projectsToProjectDTOs(List<Project> projects);
    Project projectDTOToProject(ProjectDTO projectDTO);

    SkillDTO skillToSkillDTO (Skill skill);
    List<SkillDTO> skillsToSkillDTOs (List<Skill> skills);
    Skill skillDTOToSkill(SkillDTO skillDTO);
}

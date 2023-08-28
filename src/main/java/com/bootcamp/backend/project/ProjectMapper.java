package com.bootcamp.backend.project;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDTO projectToProjectDTO(Project project);
    List<ProjectDTO> projectsToProjectDTOs(List<Project> projects);
    Project projectDTOToProject(ProjectDTO projectDTO);
}
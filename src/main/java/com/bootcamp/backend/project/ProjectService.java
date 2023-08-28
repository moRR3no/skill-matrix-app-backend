package com.bootcamp.backend.project;

import com.bootcamp.backend.exceptions.AlreadyExistsException;
import com.bootcamp.backend.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    //    public List<Project> getProjects() {
//        return projectRepository.findAll();
//    }
    public List<ProjectDTO> getProjects() {
        List<Project> projects = projectRepository.findAll();
        return projectMapper.projectsToProjectDTOs(projects);
    }

    //    public Project getProjectById(UUID id) {
//        Optional<Project> projectResult = projectRepository.findById(id);
//        return projectResult.orElseThrow(() -> new NotFoundException("Project not found with id=" + id));
//    }
    public ProjectDTO getProjectById(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found with id=" + id));
        return projectMapper.projectToProjectDTO(project);
    }

//    public Project saveProject(Project project) {
//        String projectName = project.getName();
//        Optional<Project> existingProject = projectRepository.findByName(projectName);
//        if (existingProject.isPresent()) {
//            throw new AlreadyExistsException("Project with name " + projectName + " already exists.");
//        } else {
//            return projectRepository.save(project);
//        }
//    }

    public ProjectDTO saveProject(ProjectDTO projectDTO) {
        String projectName = projectDTO.getName();
        if (projectRepository.findByName(projectName).isPresent()) {
            throw new AlreadyExistsException("Project with name " + projectName + " already exists.");
        }
        Project project = projectMapper.projectDTOToProject(projectDTO);
        Project savedProject = projectRepository.save(project);
        return projectMapper.projectToProjectDTO(savedProject);
    }


//    public void deleteById(UUID id) {
//        Optional<Project> projectToDelete = projectRepository.findById(id);
//        if (projectToDelete.isPresent()) {
//            projectRepository.deleteById(id);
//        } else {
//            throw new NotFoundException("Project not found with id=" + id);
//        }
//    }

    public void deleteById(UUID id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new NotFoundException("Project not found with id=" + id);
        }
    }
}
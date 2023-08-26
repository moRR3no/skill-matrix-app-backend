package com.bootcamp.backend.project;

import com.bootcamp.backend.employee.Employee;
import com.bootcamp.backend.exceptions.AlreadyExistsException;
import com.bootcamp.backend.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(UUID id) {
        Optional<Project> projectResult = projectRepository.findById(id);
        return projectResult.orElseThrow(() -> new NotFoundException("Project not found with id=" + id));
    }

    public Project saveProject(Project project) {
        String projectName = project.getName();
        Optional<Project> existingProject = projectRepository.findByName(projectName);
        if (existingProject.isPresent()) {
            throw new AlreadyExistsException("Project with name " + projectName + " already exists.");
        }
        return projectRepository.save(project);
    }

    public void deleteById(UUID id) {
        Optional<Project> projectToDelete = projectRepository.findById(id);
        if (projectToDelete.isPresent()) {
            projectRepository.deleteById(id);
        } else {
            throw new NotFoundException("Project not found with id=" + id);
        }
    }
}

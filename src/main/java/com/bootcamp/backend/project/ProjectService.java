package com.bootcamp.backend.project;

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
        Optional<Project> result = projectRepository.findById(id);
        Project project = null;
        if (result.isPresent()) {
            project = result.get();
        } else {
            throw new RuntimeException("Did not find project with id=" + id);
        }
        return project;
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteById(UUID id) {
        projectRepository.deleteById(id);
    }
}

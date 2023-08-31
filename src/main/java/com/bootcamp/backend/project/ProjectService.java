package com.bootcamp.backend.project;

import com.bootcamp.backend.exceptions.AlreadyExistsException;
import com.bootcamp.backend.exceptions.NotFoundException;
import com.bootcamp.backend.mappers.MapStructMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MapStructMapper mapstructMapper;

    public ProjectService(ProjectRepository projectRepository, MapStructMapper mapstructMapper) {
        this.projectRepository = projectRepository;
        this.mapstructMapper = mapstructMapper;
    }

    public List<ProjectDTO> getProjects() {
        List<Project> projects = projectRepository.findAll();
        return mapstructMapper.projectsToProjectDTOs(projects);
    }

    public ProjectDTO getProjectById(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found with id=" + id));
        return mapstructMapper.projectToProjectDTO(project);
    }

    public ProjectDTO saveProject(ProjectDTO projectDTO) {
        String projectName = projectDTO.getName();
        if (projectRepository.findByName(projectName).isPresent()) {
            throw new AlreadyExistsException("Project with name " + projectName + " already exists.");
        }
        Project project = mapstructMapper.projectDTOToProject(projectDTO);
        Project savedProject = projectRepository.save(project);
        return mapstructMapper.projectToProjectDTO(savedProject);
    }

    public void deleteById(UUID id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new NotFoundException("Project not found with id=" + id);
        }
    }
}
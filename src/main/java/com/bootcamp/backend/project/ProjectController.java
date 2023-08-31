package com.bootcamp.backend.project;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable("projectId") UUID projectId) {
        return new ResponseEntity<>(projectService.getProjectById(projectId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> addProject(@Valid @RequestBody ProjectDTO projectDTO) {
        return new ResponseEntity<>(projectService.saveProject(projectDTO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ProjectDTO> updateProject(@Valid @RequestBody ProjectDTO updatedProjectDTO) {
        return new ResponseEntity<>(projectService.saveProject(updatedProjectDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable UUID projectId) {
        projectService.deleteById(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
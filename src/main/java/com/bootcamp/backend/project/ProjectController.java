package com.bootcamp.backend.project;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ResponseEntity<List<Project>> getProjects() {
        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable("projectId") UUID projectId) {
        return new ResponseEntity<>(projectService.getProjectById(projectId), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        return new ResponseEntity<>(projectService.saveProject(project), HttpStatus.CREATED);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@RequestBody Project updatedProject) {
        return new ResponseEntity<>(projectService.saveProject(updatedProject), HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable UUID projectId) {
        projectService.deleteById(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.bootcamp.backend;

import com.bootcamp.backend.exceptions.AlreadyExistsException;
import com.bootcamp.backend.exceptions.NotFoundException;
import com.bootcamp.backend.mappers.MapStructMapper;
import com.bootcamp.backend.project.Project;
import com.bootcamp.backend.project.ProjectDTO;
import com.bootcamp.backend.project.ProjectRepository;
import com.bootcamp.backend.project.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MapStructMapper mapstructMapper;

    @InjectMocks
    private ProjectService underTest;

    private Project project;

    private ProjectDTO projectDTO;

    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        String name = "Project 1";
        project = new Project();
        project.setId(id);
        project.setName(name);

        projectDTO = new ProjectDTO();
        projectDTO.setName(name);
        projectDTO.setId(id);
    }

    @Test
    void canGetProjects() {
        //given
        List<Project> projectList = List.of(project);
        when(projectRepository.findAll()).thenReturn(projectList);

        //when
        List<ProjectDTO> projectDTOList = this.underTest.getProjects();

        //then
        assertThat(mapstructMapper.projectsToProjectDTOs(projectList)).isEqualTo(projectDTOList);
        verify(this.projectRepository).findAll();
    }

    @Test
    void canSaveProject() {
        // given
        when(projectRepository.findByName(projectDTO.getName())).thenReturn(Optional.empty());
        when(mapstructMapper.projectDTOToProject(projectDTO)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(mapstructMapper.projectToProjectDTO(project)).thenReturn(projectDTO);

        // when
        ProjectDTO savedProjectDTO = underTest.saveProject(projectDTO);

        // then
        assertThat(savedProjectDTO).isEqualTo(projectDTO);
        verify(projectRepository, times(1)).findByName(projectDTO.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void canSaveProjectThrowsAlreadyExistsException() {
        // given
        when(projectRepository.findByName(projectDTO.getName())).thenReturn(Optional.of(project));

        // when - then
        assertThrows(AlreadyExistsException.class, () -> underTest.saveProject(projectDTO));
        verify(projectRepository, times(1)).findByName(projectDTO.getName());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void canGetProjectById() {
        // given
        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(mapstructMapper.projectToProjectDTO(project)).thenReturn(projectDTO);

        // when
        ProjectDTO result = underTest.getProjectById(id);

        // then
        assertNotNull(result);
    }

    @Test
    void canGetProjectByIdNotFound() {
        // given
        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        // when - then
        assertThrows(NotFoundException.class, () -> underTest.getProjectById(id));
    }

    @Test
    void canDeleteProjectById() {
        // given
        when(projectRepository.existsById(id)).thenReturn(true);

        // when
        underTest.deleteById(id);

        // then
        verify(projectRepository, times(1)).deleteById(id);
    }

    @Test
    void canDeleteProjectByIdNotFound() {
        // given
        when(projectRepository.existsById(id)).thenReturn(false);

        // when - then
        assertThrows(NotFoundException.class, () -> underTest.deleteById(id));
    }
}
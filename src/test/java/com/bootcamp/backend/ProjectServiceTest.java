package com.bootcamp.backend;

import com.bootcamp.backend.exceptions.AlreadyExistsException;
import com.bootcamp.backend.exceptions.NotFoundException;
import com.bootcamp.backend.mappers.MapStructMapper;
import com.bootcamp.backend.project.Project;
import com.bootcamp.backend.project.ProjectDTO;
import com.bootcamp.backend.project.ProjectRepository;
import com.bootcamp.backend.project.ProjectService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
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

    @Test
    void underTestIsCreated() {
        assertThat(underTest).isNotNull();
    }

    @Test
    @Disabled
    void canGetProjects() {
        //when
        underTest.getProjects();

        //then
        verify(projectRepository).findAll();
    }

    @Test
    void testGetProjects() {
        // given
        List<Project> mockProjects = new ArrayList<>();
        when(projectRepository.findAll()).thenReturn(mockProjects);

        // when
        List<ProjectDTO> result = underTest.getProjects();

        // then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetProjectById() {
        // given
        UUID projectId = UUID.randomUUID();
        Project mockProject = new Project();
        ProjectDTO mockProjectDTO = new ProjectDTO();

        // when
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(mockProject));
        when(mapstructMapper.projectToProjectDTO(mockProject)).thenReturn(mockProjectDTO);

        ProjectDTO result = underTest.getProjectById(projectId);

        // then
        assertNotNull(result);
    }

    @Test
    void testGetProjectByIdNotFound() {
        // given
        UUID projectId = UUID.randomUUID();
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> underTest.getProjectById(projectId));
    }

    @Test
    void testSaveProjectAlreadyExists() {
        // given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
        when(projectRepository.findByName("Test Project")).thenReturn(Optional.of(new Project()));

        // then
        assertThrows(AlreadyExistsException.class, () -> underTest.saveProject(projectDTO));
    }

    @Test
    void testDeleteById() {
        // given
        UUID projectId = UUID.randomUUID();
        when(projectRepository.existsById(projectId)).thenReturn(true);

        // when
        underTest.deleteById(projectId);

        // then
        verify(projectRepository, times(1)).deleteById(projectId);
    }

    @Test
    void testDeleteByIdNotFound() {
        // given
        UUID projectId = UUID.randomUUID();
        when(projectRepository.existsById(projectId)).thenReturn(false);

        // then
        assertThrows(NotFoundException.class, () -> underTest.deleteById(projectId));
    }
}
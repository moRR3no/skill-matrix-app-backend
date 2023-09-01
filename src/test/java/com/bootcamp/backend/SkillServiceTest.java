package com.bootcamp.backend;

import com.bootcamp.backend.exceptions.AlreadyExistsException;
import com.bootcamp.backend.exceptions.NotFoundException;
import com.bootcamp.backend.mappers.MapStructMapper;
import com.bootcamp.backend.skill.Skill;
import com.bootcamp.backend.skill.SkillDTO;
import com.bootcamp.backend.skill.SkillRepository;
import com.bootcamp.backend.skill.SkillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private MapStructMapper mapstructMapper;

    @InjectMocks
    private SkillService underTest;

    @BeforeEach
    void setUp() {
        // Initialize any necessary setup or mock behavior here
    }

    @Test
    void underTestIsCreated() {
        assertThat(underTest).isNotNull();
    }

    @Test
    void testGetSkills() {
        // given
        List<Skill> mockSkills = new ArrayList<>();
        when(skillRepository.findAll()).thenReturn(mockSkills);

        // when
        List<SkillDTO> result = underTest.getSkills();

        // then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetSkillById() {
        // given
        UUID skillId = UUID.randomUUID();
        Skill mockSkill = new Skill();
        SkillDTO mockSkillDTO = new SkillDTO();

        // when
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(mockSkill));
        when(mapstructMapper.skillToSkillDTO(mockSkill)).thenReturn(mockSkillDTO);

        SkillDTO result = underTest.getSkillById(skillId);

        // then
        assertNotNull(result);
    }

    @Test
    void testGetSkillByIdNotFound() {
        // given
        UUID skillId = UUID.randomUUID();
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> underTest.getSkillById(skillId));
    }

    @Test
    void testSaveSkillAlreadyExists() {
        // given
        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setName("Test Skill");
        when(skillRepository.findByName("Test Skill")).thenReturn(Optional.of(new Skill()));

        // then
        assertThrows(AlreadyExistsException.class, () -> underTest.saveSkill(skillDTO));
    }

    @Test
    void testDeleteById() {
        // given
        UUID skillId = UUID.randomUUID();
        when(skillRepository.existsById(skillId)).thenReturn(true);

        // when
        underTest.deleteById(skillId);

        // then
        verify(skillRepository, times(1)).deleteById(skillId);
    }

    @Test
    void testDeleteByIdNotFound() {
        // given
        UUID skillId = UUID.randomUUID();
        when(skillRepository.existsById(skillId)).thenReturn(false);

        // then
        assertThrows(NotFoundException.class, () -> underTest.deleteById(skillId));
    }
}

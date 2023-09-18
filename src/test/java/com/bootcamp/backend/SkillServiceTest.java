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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private MapStructMapper mapstructMapper;

    @InjectMocks
    private SkillService underTest;

    private Skill skill;
    private SkillDTO skillDTO;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        String name = "Java";
        skill = new Skill();
        skill.setId(id);
        skill.setName(name);

        skillDTO = new SkillDTO();
        skillDTO.setName(name);
        skillDTO.setId(id);
    }

    @Test
    void canGetSkills() {
        //given
        List<Skill> skillList = List.of(skill);
        when(skillRepository.findAll()).thenReturn(skillList);

        //when
        List<SkillDTO> skillDTOList = this.underTest.getSkills();

        //then
        assertThat(mapstructMapper.skillsToSkillDTOs(skillList)).isEqualTo(skillDTOList);
        verify(this.skillRepository).findAll();
    }

    @Test
    void canSaveSkill() {
        // given
        when(skillRepository.findByName(skillDTO.getName())).thenReturn(Optional.empty());
        when(mapstructMapper.skillDTOToSkill(skillDTO)).thenReturn(skill);
        when(skillRepository.save(skill)).thenReturn(skill);
        when(mapstructMapper.skillToSkillDTO(skill)).thenReturn(skillDTO);

        // when
        SkillDTO savedSkillDTO = underTest.saveSkill(skillDTO);

        // then
        assertThat(savedSkillDTO).isEqualTo(skillDTO);
        verify(skillRepository, times(1)).findByName(skillDTO.getName());
        verify(skillRepository, times(1)).save(skill);
    }

    @Test
    void canSaveSkillThrowsAlreadyExistsException() {
        // given
        when(skillRepository.findByName(skillDTO.getName())).thenReturn(Optional.of(skill));

        // when - then
        assertThrows(AlreadyExistsException.class, () -> underTest.saveSkill(skillDTO));
        verify(skillRepository, times(1)).findByName(skillDTO.getName());
        verify(skillRepository, never()).save(any(Skill.class));
    }

    @Test
    void canGetSkillById() {
        // given
        when(skillRepository.findById(id)).thenReturn(Optional.of(skill));
        when(mapstructMapper.skillToSkillDTO(skill)).thenReturn(skillDTO);

        //when
        SkillDTO result = underTest.getSkillById(id);

        // then
        assertThat(result).isEqualTo(skillDTO);
    }

    @Test
    void canGetSkillByIdNotFound() {
        // given
        when(skillRepository.findById(id)).thenReturn(Optional.empty());

        // when - then
        assertThrows(NotFoundException.class, () -> underTest.getSkillById(id));
    }

    @Test
    void canDeleteSkillById() {
        // given
        when(skillRepository.existsById(id)).thenReturn(true);

        // when
        underTest.deleteById(id);

        // then
        verify(skillRepository, times(1)).deleteById(id);
    }

    @Test
    void canDeleteSkillByIdNotFound() {
        // given
        when(skillRepository.existsById(id)).thenReturn(false);

        // when - then
        assertThrows(NotFoundException.class, () -> underTest.deleteById(id));
    }
}

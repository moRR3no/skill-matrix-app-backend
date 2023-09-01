package com.bootcamp.backend.skill;

import com.bootcamp.backend.exceptions.AlreadyExistsException;
import com.bootcamp.backend.exceptions.NotFoundException;
import com.bootcamp.backend.mappers.MapStructMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final MapStructMapper mapstructMapper;

    public SkillService(SkillRepository skillRepository, MapStructMapper mapstructMapper) {
        this.skillRepository = skillRepository;
        this.mapstructMapper = mapstructMapper;
    }

    public List<SkillDTO> getSkills() {
        List<Skill> skills = skillRepository.findAll();
        return mapstructMapper.skillsToSkillDTOs(skills);
    }

    public SkillDTO getSkillById(UUID id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Skill not found with id=" + id));
        return mapstructMapper.skillToSkillDTO(skill);
    }

    public SkillDTO saveSkill(SkillDTO skillDTO) {
        String skillName = skillDTO.getName();
        if (skillRepository.findByName(skillName).isPresent()) {
            throw new AlreadyExistsException("Skill with name " + skillName + " already exists.");
        }
        Skill skill = mapstructMapper.skillDTOToSkill(skillDTO);
        Skill savedSkill = skillRepository.save(skill);
        return mapstructMapper.skillToSkillDTO(savedSkill);
    }

    public void deleteById(UUID id) {
        if (skillRepository.existsById(id)) {
            skillRepository.deleteById(id);
        } else {
            throw new NotFoundException("Skill not found with id=" + id);
        }
    }
}
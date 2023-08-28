package com.bootcamp.backend.skill;

import com.bootcamp.backend.exceptions.AlreadyExistsException;
import com.bootcamp.backend.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public SkillService(SkillRepository skillRepository, SkillMapper skillMapper) {
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }

//    public List<Skill> getSkills() {
//        return skillRepository.findAll();
//    }
    public List<SkillDTO> getSkills() {
        List<Skill> skills = skillRepository.findAll();
        return skillMapper.skillsToSkillDTOs(skills);
    }

//    public Skill getSkillById(UUID id) {
//        Optional<Skill> skillResult = skillRepository.findById(id);
//        return skillResult.orElseThrow(() -> new NotFoundException("Skill not found with id=" + id));
//    }
    public SkillDTO getSkillById(UUID id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Skill not found with id=" + id));
        return skillMapper.skillToSkillDTO(skill);
    }

//    public Skill saveSkill(Skill skill) {
//        String skillName = skill.getName();
//        if (skillRepository.existsByName(skillName)) {
//            throw new AlreadyExistsException("Skill with name " + skillName + " already exists.");
//        } else {
//            return skillRepository.save(skill);
//        }
//    }
    public SkillDTO saveSkill(SkillDTO skillDTO) {
        String skillName = skillDTO.getName();
        if (skillRepository.existsByName(skillName)) {
            throw new AlreadyExistsException("Skill with name " + skillName + " already exists.");
        }
        Skill skill = skillMapper.skillDTOToSkill(skillDTO);
        Skill savedSkill = skillRepository.save(skill);
        return skillMapper.skillToSkillDTO(savedSkill);
    }

    public void deleteById(UUID id) {
        Optional<Skill> skillToDelete = skillRepository.findById(id);
        if (skillToDelete.isPresent()) {
            skillRepository.deleteById(id);
        } else {
            throw new NotFoundException("Skill not found with id=" + id);
        }
    }
}
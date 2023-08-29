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

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getSkills() {
        return skillRepository.findAll();
    }

    public Skill getSkillById(UUID id) {
        Optional<Skill> skillResult = skillRepository.findById(id);
        return skillResult.orElseThrow(() -> new NotFoundException("Skill not found with id=" + id));
    }

    public Skill saveSkill(Skill skill) {
        String skillName = skill.getName();
        if (skillRepository.existsByName(skillName)) {
            throw new AlreadyExistsException("Skill with name " + skillName + " already exists.");
        } else {
            return skillRepository.save(skill);
        }
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
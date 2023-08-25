package com.bootcamp.backend.skill;

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
        Optional<Skill> result = skillRepository.findById(id);
        Skill skill = null;
        if (result.isPresent()) {
            skill = result.get();
        } else {
            throw new RuntimeException("Did not find skill with id=" + id);
        }
        return skill;
    }

    public Skill saveSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public void deleteById(UUID id) {
        skillRepository.deleteById(id);
    }
}

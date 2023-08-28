package com.bootcamp.backend.skill;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    SkillDTO skillToSkillDTO (Skill skill);
    List<SkillDTO> skillsToSkillDTOs (List<Skill> skills);
    Skill skillDTOToSkill(SkillDTO skillDTO);
}

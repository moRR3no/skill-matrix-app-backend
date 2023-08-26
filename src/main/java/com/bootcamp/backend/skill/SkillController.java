package com.bootcamp.backend.skill;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<Skill>> getSkills() {
        return new ResponseEntity<>(skillService.getSkills(), HttpStatus.OK);
    }

    @GetMapping("/{skillId}")
    public ResponseEntity<Skill> getSkillById(@PathVariable("skillId") UUID skillId) {
        try {
            Skill skill = skillService.getSkillById(skillId);
            return new ResponseEntity<>(skill, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Skill> addSkill(@RequestBody Skill skill) {
        return new ResponseEntity<>(skillService.saveSkill(skill), HttpStatus.CREATED);
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<Skill> updateSkill(@PathVariable("skillId") UUID skillId, @RequestBody Skill updatedSkill) {
        updatedSkill.setId(skillId);
        Skill updated = skillService.updateSkill(updatedSkill);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<HttpStatus> deleteSkill(@PathVariable UUID skillId) {
        try {
            skillService.deleteById(skillId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
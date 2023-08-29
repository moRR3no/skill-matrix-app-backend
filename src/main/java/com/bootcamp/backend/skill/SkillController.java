package com.bootcamp.backend.skill;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(skillService.getSkillById(skillId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Skill> addSkill(@RequestBody Skill skill) {
        return new ResponseEntity<>(skillService.saveSkill(skill), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Skill> updateSkill(@RequestBody Skill updatedSkill) {
        return new ResponseEntity<>(skillService.saveSkill(updatedSkill), HttpStatus.OK);
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<HttpStatus> deleteSkill(@PathVariable UUID skillId) {
        skillService.deleteById(skillId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
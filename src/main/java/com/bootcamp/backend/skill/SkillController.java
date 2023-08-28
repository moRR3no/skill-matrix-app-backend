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
    public ResponseEntity<List<SkillDTO>> getSkills() {
        return new ResponseEntity<>(skillService.getSkills(), HttpStatus.OK);
    }

    @GetMapping("/{skillId}")
    public ResponseEntity<SkillDTO> getSkillById(@PathVariable("skillId") UUID skillId) {
        return new ResponseEntity<>(skillService.getSkillById(skillId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SkillDTO> addSkill(@RequestBody SkillDTO skillDTO) {
        return new ResponseEntity<>(skillService.saveSkill(skillDTO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SkillDTO> updateSkill(@RequestBody SkillDTO updatedSkillDTO) {
        return new ResponseEntity<>(skillService.saveSkill(updatedSkillDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<HttpStatus> deleteSkill(@PathVariable UUID skillId) {
        skillService.deleteById(skillId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
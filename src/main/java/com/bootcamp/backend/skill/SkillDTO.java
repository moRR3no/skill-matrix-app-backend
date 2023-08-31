package com.bootcamp.backend.skill;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SkillDTO {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String name;
}

package com.bootcamp.backend.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProjectDTO {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String name;
}
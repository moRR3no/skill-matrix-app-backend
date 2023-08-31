package com.bootcamp.backend.skill;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "skills")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    @NotBlank
    private String name;
}
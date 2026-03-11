package br.com.backend.DTO.response;

import lombok.Getter;

import java.util.UUID;

@Getter
public class StudentResponseDTO {

    private UUID id;

    private String name;

    private String email;

    private Integer age;

    private String classroom;

    public StudentResponseDTO(UUID id, String name, String email, String classroom) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.classroom = classroom;
    }
}

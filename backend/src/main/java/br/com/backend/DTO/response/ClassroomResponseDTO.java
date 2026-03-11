package br.com.backend.DTO.response;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ClassroomResponseDTO {

    private UUID id;

    private String name;

    public ClassroomResponseDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}

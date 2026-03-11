package br.com.backend.DTO.request;

import br.com.backend.entity.SchoolYear;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ClassroomRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "SchoolYear is required")
    private SchoolYear schoolYear;
}

package br.com.backend.dto.request;

public record ClassroomUpdateRequest(
        String name,
        Integer newCapacity
) {
}

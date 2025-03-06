package com.portifolio.bebidas.entrypoint.controller.dto.response;

public record PaginationResponseDTO(
        int page,
        int pagaSize,
        Long totalElements,
        int totalPages) {
}

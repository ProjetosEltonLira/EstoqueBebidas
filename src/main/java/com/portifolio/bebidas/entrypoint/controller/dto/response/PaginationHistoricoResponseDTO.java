package com.portifolio.bebidas.entrypoint.controller.dto.response;

public record PaginationHistoricoResponseDTO(
        int page,
        int pagaSize,
        Long totalElements,
        int totalPages) {
}

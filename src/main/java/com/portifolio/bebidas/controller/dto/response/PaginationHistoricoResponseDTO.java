package com.portifolio.bebidas.controller.dto.response;

public record PaginationHistoricoResponseDTO(
        int page,
        int pagaSize,
        Long totalElements,
        int totalPages) {
}

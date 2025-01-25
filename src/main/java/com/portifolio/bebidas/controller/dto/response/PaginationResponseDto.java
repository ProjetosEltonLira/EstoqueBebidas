package com.portifolio.bebidas.controller.dto.response;

public record PaginationResponseDto(
        int page,
        int pagaSize,
        Long totalElements,
        int totalPages) {
}

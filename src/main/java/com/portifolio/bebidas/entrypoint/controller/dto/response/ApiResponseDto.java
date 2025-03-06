package com.portifolio.bebidas.entrypoint.controller.dto.response;

import java.util.List;

public record ApiResponseDto<T>(
        List<T> data,
        PaginationResponseDTO pagination
) {
}

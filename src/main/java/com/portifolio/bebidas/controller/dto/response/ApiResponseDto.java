package com.portifolio.bebidas.controller.dto.response;

import java.util.List;

public record ApiResponseDto<T>(
        List<T> data,
        PaginationResponseDto pagination
) {
}

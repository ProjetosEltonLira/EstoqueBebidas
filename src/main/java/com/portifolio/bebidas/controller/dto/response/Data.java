package com.portifolio.bebidas.controller.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@JsonFormat
@Component
public class Data<T> {
    @JsonProperty("data")
    @Valid @NotNull (message = "Campo Obrigatório")
    private T Data;

    public Data() {}
    public Data(final T data) {
        Data = data;
    }

    @Valid
    @NotNull(message = "Campo Obrigatório")
    public T getData() {
        return Data;
    }
    @JsonProperty("data")
    public void setData(@Valid @NotNull(message = "Campo Obrigatório") T data) {
        Data = data;
    }
}

package com.portifolio.bebidas.entrypoint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portifolio.bebidas.core.entities.*;
import com.portifolio.bebidas.core.service.SecaoService;
import com.portifolio.bebidas.entrypoint.controller.dto.request.DadosSecaoDto;
import com.portifolio.bebidas.entrypoint.controller.dto.request.SecaoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SecaoControllerTest {

    @InjectMocks
    private SecaoController secaoController;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SecaoService secaoService;

    TipoBebidaEntity tipoBebidaAlcoolica;
    SecaoEntity secaoAlcoolica;
    BebidaSecaoId id1;
    BebidaSecaoId id2;
    List<BebidaSecaoEntity> listBebidasNaSecao = new ArrayList<>();

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(secaoController).alwaysDo(print()).build();

        tipoBebidaAlcoolica = new TipoBebidaEntity(1L, "ALCOOLICA");
        secaoAlcoolica = new SecaoEntity(1L, "teste", tipoBebidaAlcoolica, null);

        id1 = new BebidaSecaoId(new BebidaEntity(1L, "Cachaça", tipoBebidaAlcoolica), secaoAlcoolica);
        id2 = new BebidaSecaoId(new BebidaEntity(2L, "SELVAGEM", tipoBebidaAlcoolica), secaoAlcoolica);

        listBebidasNaSecao.add(new BebidaSecaoEntity(id1, 90.0));
        listBebidasNaSecao.add(new BebidaSecaoEntity(id2, 100.0));

        secaoAlcoolica.setBebidaSecaoEntities(listBebidasNaSecao);
    }

    @Test
    void testCriarSecao() throws Exception {

        //Arrange
        SecaoDTO secaoDTO = new SecaoDTO(new DadosSecaoDto("SECAO TESTE", "ALCOOLICA"));

        when(secaoService.criarSecao(secaoDTO)).thenReturn(secaoAlcoolica);

        mockMvc.perform(post("/secao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(secaoDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/secao/1"));

    }

//    @Test
//    void testCadastrarBebidaNaSecao() throws Exception {
//        InserirBebidaSecaoDto bebidasDto = new InserirBebidaSecaoDto();
//        ResponseSecaoDTO responseMock = new ResponseSecaoDTO();
//        responseMock.setSecaoId(2L);
//
//        when(secaoService.cadastrarBebidas(eq(2L), any(InserirBebidaSecaoDto.class))).thenReturn(responseMock);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/secao/2")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{}"))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("Location", "/secao/2"));
//    }
//
//    @Test
//    void testBuscarBebidasNaSecao_Success() throws Exception {
//        ResponseSecaoDTO responseMock = new ResponseSecaoDTO();
//        responseMock.setSecaoId(3L);
//
//        when(secaoService.procurarSecaoPorId(3L)).thenReturn(responseMock);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/secao/3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.secaoId").value(3L));
//    }
//
//    @Test
//    void testBuscarBebidasNaSecao_NotFound() throws Exception {
//        when(secaoService.procurarSecaoPorId(4L)).thenReturn(null);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/secao/4"))
//                .andExpect(status().isNotFound());
//    }
}
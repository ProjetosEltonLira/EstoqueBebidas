package com.portifolio.bebidas.core.entities.mapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SecaoMapperTest {

    SecaoMapper mapper = Mappers.getMapper(SecaoMapper.class);

//    @Nested
//    class testeMapper {
//        @Test
//        @DisplayName("Teste 12 - Testar o secaoMapper.")
//        void deveMapearSecaoEntityParaResponseSecaoDtoCorretamente() {
//
//            // Criando um mock de BebidasNaSecaoResponseDto
//            BebidasNaSecaoResponseDto bebidaResponseDto = new BebidasNaSecaoResponseDto(1L, "teste", "ALCOOLICA", 50.0, LocalDateTime.now());
//
//            SecaoEntity secaoEntity = new SecaoEntity(1L,"teste",
//                    new TipoBebidaEntity(1L,"ALCOOLICA"),
//                    List.of(new BebidaSecaoEntity()))
//
//
//            // Chamando o mapper
//            ResponseSecaoDto responseDto = mapper.toResponseSecaoDto(secaoEntity);
//
//            // Verificando os mapeamentos
//            assertNotNull(responseDto);
//            assertEquals(responseDto.secaoId(), secaoAlcoolica.getSecaoId());
//            assertEquals(responseDto.nomeSecao(), secaoAlcoolica.getNomeSecao());
//            assertEquals(responseDto.tipoSecao(), secaoAlcoolica.getTipoBebida().getDescricao());
//            assertEquals(responseDto.quantidadeTotal(), 300.0);
//        }
//    }
}
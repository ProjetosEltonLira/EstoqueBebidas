package com.portifolio.bebidas.core.service;

import com.portifolio.bebidas.core.entities.TipoBebidaEntity;
import com.portifolio.bebidas.core.enums.TipoBebida;
import com.portifolio.bebidas.core.exceptions.TipoDeBebidaNaoEncontradoException;
import com.portifolio.bebidas.core.repository.TipoBebidaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TipoBebidaServiceTest {

    @InjectMocks
    TipoBebidaService tipoBebidaService;

    @Mock
    TipoBebidaRepository tipoBebidaRepository;

    TipoBebidaEntity tipoBebidaEntity;


    @Nested
    class getTipoBebida {

        @Test
        @DisplayName("Validar o FindById do tipo Bebida")
        void validarFindByIdTipoBebida () {

            var codigoTipoBebida = 1L;

            tipoBebidaEntity = new TipoBebidaEntity(1L,"ALCOOLICA");
            doReturn(Optional.of(tipoBebidaEntity)).when(tipoBebidaRepository).findById(codigoTipoBebida);

            var tipoBebida = tipoBebidaService.getTipoBebida(TipoBebida.ALCOOLICA.getDescricao());

            assertEquals(1L,tipoBebida.getTipoBebidaId());
            assertEquals("ALCOOLICA",tipoBebida.getDescricao());

        }

        @Test
        @DisplayName("Lançar exception quando o ID do tipo Bebida não for enconrado")
        void LancarExceptionQuando_FindByIdTipoBebidaRetornarInvalido() {

            var codigoTipoBebida = 1L;
            tipoBebidaEntity = new TipoBebidaEntity(1L, "ALCOOLICA");

            //O mét_odo findById retorna um Optional<TipoBebidaEntity>, então a simulação correta deveria ser Optional.empty().
            doReturn(Optional.empty()).when(tipoBebidaRepository).findById(codigoTipoBebida);
            //when(tipoBebidaRepository.findById(codigoTipoBebida)).thenReturn(Optional.empty());


            var exception = assertThrows(TipoDeBebidaNaoEncontradoException.class,
                    () -> tipoBebidaService.getTipoBebida(TipoBebida.ALCOOLICA.getDescricao()));

            assertEquals("Tipo de bebida não encontrado.", exception.getMessage());

        }
    }
}
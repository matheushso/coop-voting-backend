package br.com.coop.voting.backend.api.controller;

import br.com.coop.voting.backend.domain.DTO.ResultadoDTO;
import br.com.coop.voting.backend.domain.service.AbstractService;
import br.com.coop.voting.backend.domain.service.ResultadoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resultados")
public class ResultadoController extends AbstractService {

    @Autowired
    private ResultadoService resultadoService;

    @GetMapping("/{pautaId}")
    public ResponseEntity<?> calcularResultado(@PathVariable Long pautaId) {
        try {
            ResultadoDTO resultado = resultadoService.calcularResultado(pautaId);
            return ResponseEntity.status(HttpStatus.OK).body(resultado);

        } catch (EntityNotFoundException ex) {
            return montarMensagemDeErro(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}

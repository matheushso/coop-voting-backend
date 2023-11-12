package br.com.coop.voting.backend.api.controller;

import br.com.coop.voting.backend.domain.exception.AssociadoNaoAutorizadoException;
import br.com.coop.voting.backend.domain.model.Voto;
import br.com.coop.voting.backend.domain.service.AbstractService;
import br.com.coop.voting.backend.domain.service.VotoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VotoController extends AbstractService {

    @Autowired
    private VotoService votoService;

    @PostMapping("/v1/votos")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> cadastrarPauta(@RequestBody Voto voto) {
        try {
            voto = votoService.cadastrarVoto(voto);
            return ResponseEntity.status(HttpStatus.CREATED).body(voto);

        } catch (EntityNotFoundException ex) {
            return montarMensagemDeErro(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return montarMensagemDeErro(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("/v2/votos")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> cadastrarPautaValidandoCpfAssociado(@RequestBody Voto voto) {
        try {
            voto = votoService.cadastrarPautaValidandoCpfAssociado(voto);
            return ResponseEntity.status(HttpStatus.CREATED).body(voto);

        } catch (EntityNotFoundException ex) {
            return montarMensagemDeErro(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return montarMensagemDeErro(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (AssociadoNaoAutorizadoException ex) {
            return montarMensagemDeErro(HttpStatus.FORBIDDEN, ex.getMessage());
        }
    }
}

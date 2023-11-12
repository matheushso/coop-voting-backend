package br.com.coop.voting.backend.api.controller;

import br.com.coop.voting.backend.domain.model.Pauta;
import br.com.coop.voting.backend.domain.service.AbstractService;
import br.com.coop.voting.backend.domain.service.PautaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController extends AbstractService {

    @Autowired
    private PautaService pautaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> cadastrarPauta(@RequestBody Pauta pauta) {
        try {
            pauta = pautaService.cadastrarPauta(pauta);
            return ResponseEntity.status(HttpStatus.CREATED).body(pauta);
        } catch (EntityNotFoundException ex) {
            return montarMensagemDeErro(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}

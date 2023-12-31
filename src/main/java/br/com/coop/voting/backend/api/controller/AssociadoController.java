package br.com.coop.voting.backend.api.controller;

import br.com.coop.voting.backend.domain.model.Associado;
import br.com.coop.voting.backend.domain.service.AssociadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/associados")
public class AssociadoController extends AbstractController {

    @Autowired
    private AssociadoService associadoService;

    @GetMapping
    public List<Associado> buscarTodosAssociados() {
        return associadoService.buscarTodosAssociados();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> cadastrarAssociado(@RequestBody Associado associado) {
        try {
            associado = associadoService.cadastrarAssociado(associado);
            return ResponseEntity.status(HttpStatus.CREATED).body(associado);

        } catch (IllegalArgumentException ex) {
            return montarMensagemDeErro(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}

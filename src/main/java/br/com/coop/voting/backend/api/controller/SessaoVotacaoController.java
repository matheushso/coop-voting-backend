package br.com.coop.voting.backend.api.controller;

import br.com.coop.voting.backend.domain.model.SessaoVotacao;
import br.com.coop.voting.backend.domain.service.AbstractService;
import br.com.coop.voting.backend.domain.service.SessaoVotacaoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessoesvotacoes")
public class SessaoVotacaoController extends AbstractService {

    @Autowired
    private SessaoVotacaoService sessaoVotacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> cadastrarPauta(@RequestBody SessaoVotacao sessaoVotacao, @RequestParam(name = "tempoAberturaEmMinutos", required = false) Integer tempoAberturaEmMinutos) {
        try {
            sessaoVotacao = sessaoVotacaoService.cadastrarSessao(sessaoVotacao, tempoAberturaEmMinutos);
            return ResponseEntity.status(HttpStatus.CREATED).body(sessaoVotacao);

        } catch (EntityNotFoundException ex) {
            return montarMensagemDeErro(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return montarMensagemDeErro(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}

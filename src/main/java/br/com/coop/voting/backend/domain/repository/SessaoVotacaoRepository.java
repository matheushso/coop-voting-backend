package br.com.coop.voting.backend.domain.repository;

import br.com.coop.voting.backend.domain.model.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {

    @Query("select p from Pauta p join SessaoVotacao sv on p.id = sv.pauta.id where sv.resultadoPublicado != true")
    public SessaoVotacao buscarSessaoEmAbertoPorPauta(Long pautaId);
}

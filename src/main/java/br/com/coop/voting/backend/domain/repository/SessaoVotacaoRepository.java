package br.com.coop.voting.backend.domain.repository;

import br.com.coop.voting.backend.domain.model.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {

    @Query("select sv from SessaoVotacao sv where sv.pauta.id = :pautaId and sv.resultadoPublicado != true")
    public SessaoVotacao buscarSessaoEmAbertoPorPauta(Long pautaId);
}

package br.com.coop.voting.backend.domain.repository;

import br.com.coop.voting.backend.domain.model.Associado;
import br.com.coop.voting.backend.domain.model.Pauta;
import br.com.coop.voting.backend.domain.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    public Voto findFirstByAssociadoAndPauta(Associado associado, Pauta pauta);
}

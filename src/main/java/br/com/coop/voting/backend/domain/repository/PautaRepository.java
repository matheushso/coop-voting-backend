package br.com.coop.voting.backend.domain.repository;

import br.com.coop.voting.backend.domain.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaRepository extends JpaRepository<Pauta, Long> {
}

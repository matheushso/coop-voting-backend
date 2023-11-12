package br.com.coop.voting.backend.domain.repository;

import br.com.coop.voting.backend.domain.model.Associado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociadoRepository extends JpaRepository<Associado, Long> {
}

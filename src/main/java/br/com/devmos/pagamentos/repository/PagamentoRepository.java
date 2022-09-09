package br.com.devmos.pagamentos.repository;

import br.com.devmos.pagamentos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}

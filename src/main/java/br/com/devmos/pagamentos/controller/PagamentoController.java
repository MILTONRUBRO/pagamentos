package br.com.devmos.pagamentos.controller;

import br.com.devmos.pagamentos.dto.PagamentoDTO;
import br.com.devmos.pagamentos.service.PagamentoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    PagamentoService pagamentoService;

    @GetMapping
    public Page<PagamentoDTO> listar(@PageableDefault(size = 10) Pageable pageable){
        return pagamentoService.getPagamentos(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> getPagamento(@PathVariable Long id){
        return ResponseEntity.ok(pagamentoService.getPagamentosById(id));
    }

    @PostMapping
    public ResponseEntity<PagamentoDTO> savePagamento(@RequestBody @Valid PagamentoDTO dto, UriComponentsBuilder uriBuilder){
        PagamentoDTO pagamento = pagamentoService.createPagamento(dto);
        URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();
        return ResponseEntity.created(endereco).body(pagamento);

    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> updatePagamento(@PathVariable Long id, @RequestBody @Valid PagamentoDTO dto){
        pagamentoService.updatePagamento(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PagamentoDTO> deletePagamento(@PathVariable Long id){
        pagamentoService.deletePagamento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
    @CircuitBreaker(name = "atualizaPedido", fallbackMethod = "pagamentoAutorizadoComIntegracaoPendente")
    public void confirmarPagamento(@PathVariable @NotNull Long id){
        pagamentoService.confirmarPagamento(id);
    }

    public void pagamentoAutorizadoComIntegracaoPendente(Long id, Exception e){
        pagamentoService.alteraStatus(id);
    }

}

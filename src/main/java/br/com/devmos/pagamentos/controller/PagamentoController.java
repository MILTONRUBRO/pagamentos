package br.com.devmos.pagamentos.controller;

import br.com.devmos.pagamentos.dto.PagamentoDTO;
import br.com.devmos.pagamentos.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

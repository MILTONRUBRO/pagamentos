package br.com.devmos.pagamentos.service;

import br.com.devmos.pagamentos.dto.PagamentoDTO;
import br.com.devmos.pagamentos.model.Pagamento;
import org.modelmapper.ModelMapper;
import br.com.devmos.pagamentos.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ModelMapper modelMapper;


    public Page<PagamentoDTO> getPagamentos(Pageable paginacao){
        return pagamentoRepository.findAll(paginacao).map( p -> modelMapper.map(p , PagamentoDTO.class));
    }

    public PagamentoDTO getPagamentosById(Long id){
        Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return modelMapper.map(pagamento, PagamentoDTO.class);
    }
}

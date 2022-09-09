package br.com.devmos.pagamentos.service;

import br.com.devmos.pagamentos.dto.PagamentoDTO;
import org.modelmapper.ModelMapper;
import br.com.devmos.pagamentos.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ModelMapper modelMapper;


    public Page<PagamentoDTO> getPagamentos(Pageable paginacao){
        return pagamentoRepository.findAll(paginacao).map( p -> modelMapper.map(p , PagamentoDTO.class));
    }
}

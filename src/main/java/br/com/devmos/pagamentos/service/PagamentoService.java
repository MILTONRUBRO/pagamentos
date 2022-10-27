package br.com.devmos.pagamentos.service;

import br.com.devmos.pagamentos.dto.PagamentoDTO;
import br.com.devmos.pagamentos.http.PedidoClient;
import br.com.devmos.pagamentos.model.Pagamento;
import br.com.devmos.pagamentos.model.Status;
import org.modelmapper.ModelMapper;
import br.com.devmos.pagamentos.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PedidoClient pedidoClient;

    public Page<PagamentoDTO> getPagamentos(Pageable paginacao){
        return pagamentoRepository.findAll(paginacao).map( p -> modelMapper.map(p , PagamentoDTO.class));
    }

    public PagamentoDTO getPagamentosById(Long id){
        Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public PagamentoDTO createPagamento(PagamentoDTO dto){
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        pagamentoRepository.save(pagamento);

        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public void updatePagamento(Long id, PagamentoDTO dto){
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setId(id);
        pagamentoRepository.save(pagamento);
    }

    public void deletePagamento(Long id){
        pagamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        pagamentoRepository.deleteById(id);
    }

    public void confirmarPagamento(Long id){
        Optional<Pagamento> pagamento = pagamentoRepository.findById(id);

        if (!pagamento.isPresent()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO);
        pagamentoRepository.save(pagamento.get());
        pedidoClient.atualizaPagamento(pagamento.get().getPedidoId());
    }
}

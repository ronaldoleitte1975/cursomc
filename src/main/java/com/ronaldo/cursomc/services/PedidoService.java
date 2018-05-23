package com.ronaldo.cursomc.services;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ronaldo.cursomc.domain.Cliente;
import com.ronaldo.cursomc.domain.ItemPedido;
import com.ronaldo.cursomc.domain.PagamentoComBoleto;
import com.ronaldo.cursomc.domain.Pedido;
import com.ronaldo.cursomc.domain.enums.EstadoPagamento;
import com.ronaldo.cursomc.domain.enums.Perfil;
import com.ronaldo.cursomc.repositories.ItemPedidoRepository;
import com.ronaldo.cursomc.repositories.PagamentoRepository;
import com.ronaldo.cursomc.repositories.PedidoRepository;
import com.ronaldo.cursomc.security.UserSS;
import com.ronaldo.cursomc.services.exceptions.AuthorizationException;
import com.ronaldo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ItemPedidoRepository ipRepository;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);

		UserSS user = UserService.authenticated();		
		
		if (obj != null) {
			if (user == null || !user.hasRole(Perfil.ADMIN) && !obj.get().getCliente().getId().equals(user.getId())) {				
				throw new AuthorizationException("Acesso negado");
			}
		} else if (user == null || !user.hasRole(Perfil.ADMIN)) {
			throw new AuthorizationException("Acesso negado");
		}

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));

	}

	@Transactional
	public Pedido insert(@Valid Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);

		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}

		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());

		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());
			ip.setPedido(obj);
		}

		ipRepository.saveAll(obj.getItens());
		// System.out.println(obj);
		// emailService.sendOrderConfirmationEmail(obj);
		emailService.sendOrderConfirmationHtmlEmail(obj);

		return obj;
	}

	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}

}

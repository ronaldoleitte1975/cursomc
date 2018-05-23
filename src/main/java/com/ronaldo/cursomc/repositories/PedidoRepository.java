package com.ronaldo.cursomc.repositories;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ronaldo.cursomc.domain.Cliente;
import com.ronaldo.cursomc.domain.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Serializable> {
	
	@Transactional(readOnly = true)
	Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);

}

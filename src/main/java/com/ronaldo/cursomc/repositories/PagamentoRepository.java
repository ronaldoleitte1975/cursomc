package com.ronaldo.cursomc.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ronaldo.cursomc.domain.Pagamento;

@Repository
public  interface PagamentoRepository extends JpaRepository<Pagamento, Serializable>{

}

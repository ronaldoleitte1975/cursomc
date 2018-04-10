package com.ronaldo.cursomc.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ronaldo.cursomc.domain.Cidade;

@Repository
public  interface CidadeRepository extends JpaRepository<Cidade, Serializable>{

}

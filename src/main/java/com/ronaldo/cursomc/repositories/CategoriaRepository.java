package com.ronaldo.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ronaldo.cursomc.domain.Categoria;

@Repository
public  interface CategoriaRepository extends JpaRepository<Categoria, Integer>{


}

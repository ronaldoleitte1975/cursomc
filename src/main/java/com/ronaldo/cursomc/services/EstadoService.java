package com.ronaldo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ronaldo.cursomc.domain.Estado;
import com.ronaldo.cursomc.dto.EstadoDTO;
import com.ronaldo.cursomc.repositories.EstadoRepository;
import com.ronaldo.cursomc.services.exceptions.DataIntegrityException;
import com.ronaldo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository repo;

	public Estado find(Integer id) {
		Optional<Estado> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Estado.class.getName()));
	}

	public Estado insert(Estado estado) {
		estado.setId(null);
		return repo.save(estado);

	}

	public Estado update(Estado estadoNew) {
		Estado estadoOld = find(estadoNew.getId());
		updateData(estadoNew, estadoOld);
		find(estadoNew.getId());
		return repo.save(estadoOld);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma Estado que possui Produto(s).");
		}

	}

	public List<Estado> findAll() {

		return repo.findAll();

	}

	public Page<Estado> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Estado fromEstadoDTO(EstadoDTO estadoDTO) {

		return new Estado(estadoDTO.getId(), estadoDTO.getNome());

	}
	
	private void updateData(Estado estadoNew, Estado estadoOld) {
		estadoOld.setNome(estadoNew.getNome());
		
	}
}

package br.com.medcare.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.medcare.model.Medico;

@Service
public class MedicoRepositoryService {
	
	@Autowired
	MedicoRepository repo;
	
	public Medico salvaMedico(Medico medico) {
		return repo.save(medico);
		
	}
}

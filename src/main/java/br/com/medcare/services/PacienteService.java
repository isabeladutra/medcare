package br.com.medcare.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.medcare.model.Paciente;

@Service
public class PacienteService {

	@Autowired
	PacienteRepository repo;
	
	public Paciente salvaPaciente(Paciente paciente) {
		return repo.save(paciente);
		
	}
	
}

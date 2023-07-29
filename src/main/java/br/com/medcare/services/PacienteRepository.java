package br.com.medcare.services;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medcare.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Integer>{

	Optional<Paciente> findByCpf(BigInteger bigInteger);

	Paciente findByUserEmail(String email);

	Paciente findByNome(String nomePaciente);
	

}

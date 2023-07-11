package br.com.medcare.services;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medcare.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Integer>{

}

package br.com.medcare.services;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medcare.model.FichaMedica;

public interface FichaMedicaRepository extends JpaRepository<FichaMedica, Integer> {

	FichaMedica findByNomePaciente(String nomePaciente);

	
}

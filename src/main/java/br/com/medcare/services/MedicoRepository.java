package br.com.medcare.services;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medcare.model.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Integer>{

}

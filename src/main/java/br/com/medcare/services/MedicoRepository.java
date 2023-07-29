package br.com.medcare.services;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medcare.model.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Integer>{

	Optional<Medico> findByCrm(BigInteger crm);

	Medico findByNome(String nomeMedicoExistente);



}

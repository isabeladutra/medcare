package br.com.medcare.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medcare.model.ReceitaMedica;

public interface ReceitaMedicaRepository extends JpaRepository<ReceitaMedica, Integer>{
	List<ReceitaMedica> findByPacienteNomeAndMedicoNome(String nomePaciente, String nomeMedico);

	void deleteByPacienteNomeAndMedicoNomeAndDataReceita(String nomePaciente, String nomeMedico,
			Date dataReceita);
}

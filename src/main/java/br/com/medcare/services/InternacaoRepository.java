package br.com.medcare.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medcare.model.Internacao;
import br.com.medcare.model.Medico;
import br.com.medcare.model.Paciente;

public interface InternacaoRepository extends JpaRepository<Internacao, Integer>{

	List<Internacao> findByPaciente(Paciente paciente);


	Internacao findByPacienteAndDataEntradaInternacao(Paciente paciente, LocalDateTime dataEntradaInternacao);




}

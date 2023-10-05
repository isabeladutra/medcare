package br.com.medcare.services;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medcare.model.Consulta;
import br.com.medcare.model.Medico;
import br.com.medcare.model.Paciente;

public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {

	boolean existsByMedicoAndDataHora(Medico medico, LocalDateTime dataHora);

	boolean existsByPacienteAndDataHora(Paciente paciente, LocalDateTime dataHora);

	List<Consulta> findByPaciente(Paciente paciente);

	List<Consulta> findByMedicoAndDataHora(Medico medico, LocalDateTime localDate);

	Consulta findByDataHora(LocalDateTime dataHoraConsultaAtual);

	Consulta findByPacienteAndDataHora(Paciente paciente, LocalDateTime dataHora);

	
	 List<Consulta> findByMedico(Medico medico);

}

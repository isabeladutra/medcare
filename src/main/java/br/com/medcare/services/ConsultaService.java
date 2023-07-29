package br.com.medcare.services;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.medcare.dto.ReagendamentoConsultaDTO;
import br.com.medcare.exceptions.ConsultaNaoEncontradaException;
import br.com.medcare.model.Consulta;
import br.com.medcare.model.ConsultaRequest;
import br.com.medcare.model.Medico;
import br.com.medcare.model.Paciente;

@Service
public class ConsultaService {
	 
	@Autowired
	ConsultaRepository repo;
	
	@Autowired
	MedicoRepository medicoRepository;
	
	@Autowired
	ConsultaRepository consultaRepository;
	
	@Autowired
	PacienteRepository pacienteRepository;
	
	public Consulta agendarConsulta(Consulta consulta) {
        // Lógica para agendar a consulta, por exemplo, salvar no banco de dados
        return repo.save(consulta);
    }

	public boolean existeConsultaAgendada(Medico medico, LocalDateTime dataHora) {
        // Verificar se existe uma consulta agendada para o médico na data/hora fornecidas
        boolean temConsulta = repo.existsByMedicoAndDataHora(medico, dataHora);
        return temConsulta;
    }
	
	public boolean existeConsultaProPaciente(Paciente paciente, LocalDateTime dataHora) {
		
		boolean temConsulta = repo.existsByPacienteAndDataHora(paciente, dataHora);
		return temConsulta;
	}

	public List<Consulta> listarConsultasDoPaciente(Paciente paciente) {
		// TODO Auto-generated method stub
		return repo.findByPaciente(paciente);
		
	}

	
    public boolean reagendarConsulta(ReagendamentoConsultaDTO reagendamentoDTO, String emailPaciente) {
        LocalDateTime dataHoraConsultaAtual = reagendamentoDTO.getDataHoraConsultaAtual();
        LocalDateTime novaDataHoraConsulta = reagendamentoDTO.getNovaDataHoraConsulta();

        Paciente paciente = pacienteRepository.findByUserEmail(emailPaciente);

        if (paciente == null) {
            // Paciente não encontrado
            return false;
        }

        Consulta consultaAtual = consultaRepository.findByPacienteAndDataHora(paciente, dataHoraConsultaAtual);

        if (consultaAtual == null) {
            // Consulta atual não encontrada para o paciente
            return false;
        }

        // Atualiza a data e hora da consulta reagendada
        consultaAtual.setDataHora(novaDataHoraConsulta);
        consultaRepository.save(consultaAtual);

        return true;
    }

	public void atualizarConsulta(Consulta consultaExistente, Consulta novaConsulta) {
		//exclui consulta antiga
		
		repo.delete(consultaExistente);
		//inclui consulta nova
		repo.save(novaConsulta);
		
	}

	public boolean cancelarConsulta(LocalDateTime data, String emailDoPacienteAutenticado) {

        Paciente paciente = pacienteRepository.findByUserEmail(emailDoPacienteAutenticado);

        if (paciente == null) {
            // Paciente não encontrado
            return false;
        }
        
        Consulta consultaAtual = consultaRepository.findByPacienteAndDataHora(paciente, data);

        if (consultaAtual == null) {
            // Consulta atual não encontrada para o paciente
            return false;
        }
        
        repo.delete(consultaAtual);
        return true;
        
	}
}

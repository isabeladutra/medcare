package br.com.medcare;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.medcare.dto.ReagendamentoConsultaDTO;
import br.com.medcare.exceptions.ConsultaNaoEncontradaException;
import br.com.medcare.exceptions.MedicoNaoEncontradoException;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.mappers.ConsultaMapper;
import br.com.medcare.model.Consulta;
import br.com.medcare.model.ConsultaRequest;
import br.com.medcare.model.Medico;
import br.com.medcare.model.Paciente;
import br.com.medcare.services.ConsultaService;
import br.com.medcare.services.MedicoService;
import br.com.medcare.services.PacienteService;
import jakarta.annotation.security.RolesAllowed;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;


@RestController
@RequestMapping("/consultas")
public class ConsultaController {

	@Autowired
	private ConsultaService consultaService;

	@Autowired
	private MedicoService medicoService;

	@Autowired
	private PacienteService pacienteService;


	
	@PostMapping("/agendar")
	@RolesAllowed("ROLE_PACIENTE")
	public ResponseEntity<String> agendarConsulta(@RequestBody ConsultaRequest consultaRequest) {
		// Lógica para agendar a consulta usando o serviço de consulta

		Consulta consulta = ConsultaMapper.mapper(consultaRequest);
		Medico medico = new Medico();
		Paciente paciente = new Paciente();
		try {
			medico = medicoService.buscarPorCRM(consultaRequest.getCrmMedico());
		} catch (MedicoNaoEncontradoException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>("Médico não está cadastrado no sistema", HttpStatus.NOT_FOUND);
		}
		consulta.setMedico(medico);
		try {
			paciente = pacienteService.buscarPorCPF(consultaRequest.getCpfpaciente());
		} catch (PacienteNaoEncontradoException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>("Paciente não está cadastrado no sistema", HttpStatus.NOT_FOUND);
		}
		consulta.setPaciente(paciente);
		// Verificar disponibilidade do médico
		if (consultaService.existeConsultaAgendada(medico, consultaRequest.getDataDaConsulta()) == true) {
			return new ResponseEntity<>("Médico não está disponível na data e hora desejadas", HttpStatus.BAD_REQUEST);
		}
		// Verficar disponibilidade do paciente
		if (consultaService.existeConsultaProPaciente(paciente, consulta.getDataHora()) == true) {
			return new ResponseEntity<>("Você já possui consulta na data e hora desejadas", HttpStatus.BAD_REQUEST);
		}

		Consulta consultaAgendada = consultaService.agendarConsulta(consulta);
		int hora = consultaAgendada.getDataHora().getHour();
		int min = consultaAgendada.getDataHora().getMinute();
		return new ResponseEntity<String>("Consulta agendada no dia: " + consultaAgendada.getDataHora().toLocalDate()
				+ " hora: " + hora + ":" + min + " medico: " + consultaAgendada.getMedico().getNome() + " paciente: "
				+ consultaAgendada.getPaciente().getNome(), HttpStatus.CREATED);
	}

	
	@PutMapping("/reagendar")
	@RolesAllowed("ROLE_PACIENTE")
	public ResponseEntity<String> reagendarConsulta(@RequestBody ReagendamentoConsultaDTO reagendamentoDTO)
			throws ConsultaNaoEncontradaException, MedicoNaoEncontradoException, PacienteNaoEncontradoException {
		// Obtém o paciente autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String emailDoPacienteAutenticado = authentication.getName();

		boolean consultaReagendadaComSucesso = consultaService.reagendarConsulta(reagendamentoDTO, emailDoPacienteAutenticado);

		if (consultaReagendadaComSucesso) {
			return new ResponseEntity<>("Consulta reagendada com sucesso", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Falha ao reagendar a consulta", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	 
	
	@DeleteMapping("/cancelar")
	@RolesAllowed("ROLE_PACIENTE")
    public ResponseEntity<String> cancelarConsulta(@RequestParam LocalDateTime data) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String emailDoPacienteAutenticado = authentication.getName();
		
		boolean consultaCanceladaComSucesso = consultaService.cancelarConsulta(data, emailDoPacienteAutenticado);

        if (consultaCanceladaComSucesso) {
            return new ResponseEntity<>("Consulta cancelada com sucesso", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Falha ao cancelar a consulta", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

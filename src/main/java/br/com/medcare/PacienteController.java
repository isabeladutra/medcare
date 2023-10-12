package br.com.medcare;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.medcare.dto.ConsultaDTO;
import br.com.medcare.dto.PacienteDTO;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.mappers.ConsultaDTOMapper;
import br.com.medcare.mappers.PacienteDTOMapper;
import br.com.medcare.model.Consulta;
import br.com.medcare.model.Paciente;
import br.com.medcare.model.PacienteRequest;
import br.com.medcare.model.User;
import br.com.medcare.services.ConsultaService;
import br.com.medcare.services.PacienteService;
import br.com.medcare.services.UserRepositoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("paciente")
public class PacienteController {

	@Autowired
	private PacienteService pacienteService;

	public void setPacienteService(PacienteService pacienteService) {
		this.pacienteService = pacienteService;
	}

	public void setConsultaService(ConsultaService consultaService) {
		this.consultaService = consultaService;
	}

	public void setUserService(UserRepositoryService userService) {
		this.userService = userService;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	private ConsultaService consultaService;
	/*
	 * @PostMapping(value="/consultas") public void agendarConsulta() {
	 * 
	 * }
	 */

	@Autowired
	UserRepositoryService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/{cpf}/consultas")
	@RolesAllowed("ROLE_PACIENTE")
	public ResponseEntity<?> listarConsultasDoPaciente(@PathVariable("cpf") String cpf) {
		try {
			Paciente paciente = pacienteService.buscarPorCPF(cpf);
			List<Consulta> consultas = consultaService.listarConsultasDoPaciente(paciente);
			List<ConsultaDTO> listaDeConsulta = new ArrayList<ConsultaDTO>();
			for (Consulta consulta : consultas) {
				ConsultaDTO dto = ConsultaDTOMapper.mapper(consulta);
				listaDeConsulta.add(dto);
			}

			return ResponseEntity.ok(listaDeConsulta);
		} catch (PacienteNaoEncontradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não está cadastrado no sistema");
		}
	}

	@DeleteMapping("/{cpf}")
	@RolesAllowed("ROLE_MEDICO")
	public ResponseEntity<String> excluirPaciente(@PathVariable("cpf") String cpf) {
		try {
			pacienteService.excluirPaciente(cpf);
			return ResponseEntity.ok("Paciente excluído com sucesso");
		} catch (PacienteNaoEncontradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não encontrado");
		}
	}

	@PostMapping("/incluir")
	public ResponseEntity<String> cadastraPaciente(@RequestBody PacienteRequest paciente) {
		// esse endpoint seria aberto pra cadastrar um usuário médico no banco

		boolean usuarioExiste = userService.buscaMedicoouPaciente(paciente.getEmail());
		Paciente pac = pacienteService.buscarPacientePorNome(paciente.getNome());

		if (!usuarioExiste && pac == null) {
			User usuarioSalvo = userService.salvaPaciente(paciente.getEmail(), paciente.getPassword(),
					paciente.getNome());
			Paciente novoPaciente = new Paciente();
			novoPaciente.setTelefone(paciente.getTelefone());
			novoPaciente.setCpf(paciente.getCpf());
			novoPaciente.setEndereco(paciente.getEndereco());
			novoPaciente.setIdade(paciente.getIdade());
			novoPaciente.setUser(usuarioSalvo);
			novoPaciente.setNome(paciente.getNome());
			novoPaciente.setCelular(paciente.getCelular());
			try {
				novoPaciente.setDataDenascimento(paciente.getDataDeNascimentoAsDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				return new ResponseEntity<>(
						"Não foi possivel fazer o parse da data de nascimento. Deve estar no formato dd/mm/yyyy",
						HttpStatus.CONFLICT);
			}
			Paciente pacienteSalvo = pacienteService.salvarPaciente(novoPaciente);
			// Exemplo de retorno de sucesso com mensagem.
			if (pacienteSalvo != null) {
				return new ResponseEntity<>("Paciente criado com sucesso", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("Falha ao criar o paciente", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>("Já existe um paciente cadastrado com esse nome e email", HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/atualizar")
	@PreAuthorize("hasAnyRole('ROLE_MEDICO', 'ROLE_PACIENTE')")
	public ResponseEntity<String> atualizarPaciente(@RequestBody PacienteRequest pacienteRequest,
			@RequestParam String nome) {
		// Primeiro, verifique se o paciente existe pelo nome
		Paciente pacienteExistente = pacienteService.buscarPacientePorNome(nome);

		if (pacienteExistente == null) {
			return new ResponseEntity<>("Paciente não encontrado", HttpStatus.NOT_FOUND);
		}
		Paciente pacAntigo = pacienteExistente;
		if (!pacAntigo.getNome().equals(pacienteRequest.getNome())) {
			Paciente pac = pacienteService.buscarPacientePorNome(pacienteRequest.getNome());
			if (pac != null) {
				return new ResponseEntity<>("Já existe um paciente cadastrado com esse nome", HttpStatus.CONFLICT);
			}
			pacienteExistente.setNome(pacienteRequest.getNome());
			pacienteExistente.getUser().setNome(pacienteRequest.getNome());
		}

		// Atualize os campos do paciente existente com os dados do pacienteRequest
		pacienteExistente.setIdade(pacienteRequest.getIdade());
		pacienteExistente.setTelefone(pacienteRequest.getTelefone());
		pacienteExistente.setEndereco(pacienteRequest.getEndereco());
		pacienteExistente.setCpf(pacienteRequest.getCpf());

		try {
			pacienteExistente.setDataDenascimento(pacienteRequest.getDataDeNascimentoAsDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(
					"Não foi possivel fazer o parse da data de nascimento. Deve estar no formato dd/mm/yyyy",
					HttpStatus.CONFLICT);
		}
		if (!pacAntigo.getUser().getEmail().equals(pacienteRequest.getEmail())) {
			Paciente pac = pacienteService.buscarPoremail(pacienteRequest.getEmail());
			if (pac != null) {
				return new ResponseEntity<>("Já existe um paciente cadastrado com esse email", HttpStatus.CONFLICT);
			}
		}
		pacienteExistente.getUser().setEmail(pacienteRequest.getEmail());
		if (!(pacienteRequest.getPassword() == null)) {
		String senhaCodificada = passwordEncoder.encode(pacienteRequest.getPassword());
		pacienteExistente.getUser().setPassword(senhaCodificada);
		}
		// Salve as alterações no banco de dados
		pacienteService.salvarPaciente(pacienteExistente);

		return new ResponseEntity<>("Paciente atualizado com sucesso", HttpStatus.OK);
	}

	@GetMapping("/listar-pacientes")
	public ResponseEntity<List<PacienteDTO>> listarPacientes() {
		List<Paciente> pacientes = pacienteService.listarTodosPacientes();

		if (pacientes.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			List<PacienteDTO> pacientesDTO = new ArrayList<PacienteDTO>();
			for (Paciente paciente : pacientes) {
				PacienteDTO dto = PacienteDTOMapper.mapper(paciente);
				pacientesDTO.add(dto);
			}
			return new ResponseEntity<>(pacientesDTO, HttpStatus.OK);
		}
	}

}

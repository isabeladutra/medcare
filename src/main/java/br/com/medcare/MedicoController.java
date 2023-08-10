package br.com.medcare;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.medcare.model.Paciente;
import br.com.medcare.exceptions.MedicoNaoEncontradoException;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.model.Medico;
import br.com.medcare.model.MedicoRequest;
import br.com.medcare.model.PacienteRequest;
import br.com.medcare.model.User;
import br.com.medcare.services.MedicoService;
import br.com.medcare.services.PacienteService;
import br.com.medcare.services.RoleRepositoryService;
import br.com.medcare.services.UserRepositoryService;
import jakarta.annotation.security.RolesAllowed;

@RestController
public class MedicoController {

	@Autowired
	MedicoService medicoService;

	@Autowired
	UserRepositoryService userService;

	public void setMedicoService(MedicoService medicoService) {
		this.medicoService = medicoService;
	}

	public void setUserService(UserRepositoryService userService) {
		this.userService = userService;
	}

	public void setRoleService(RoleRepositoryService roleService) {
		this.roleService = roleService;
	}

	public void setPacienteService(PacienteService pacienteService) {
		this.pacienteService = pacienteService;
	}

	@Autowired
	RoleRepositoryService roleService;

	@Autowired
	PacienteService pacienteService;

	@CrossOrigin(origins = "http://127.0.0.1:3000")
	@PostMapping("/medicos")
	public ResponseEntity<String> cadastraMedico(@RequestBody MedicoRequest medico) {
		// esse endpoint seria aberto pra cadastrar um usuário médico no banco
		// roleService.Salva();

		// verifica se medico já não existe no banco
		boolean usuarioExiste = userService.buscaMedicoouPaciente(medico.getEmail());
		Medico med = medicoService.buscarMedicoPorNome(medico.getNome());
		if (!usuarioExiste && med == null) {
			User usuarioSalvo = userService.salvaMedico(medico.getEmail(), medico.getPassword());
			Medico novomedico = new Medico();
			novomedico.setCelular(medico.getCelular());
			novomedico.setCpf(medico.getCpf());
			novomedico.setCrm(medico.getCrm());
			novomedico.setEndereco(medico.getEndereco());
			novomedico.setEspecialidade(medico.getEspecialidade());
			novomedico.setIdade(medico.getIdade());
			novomedico.setUser(usuarioSalvo);
			novomedico.setNome(medico.getNome());
			Medico medicoSalvo = medicoService.salvarMedico(novomedico);
			// Exemplo de retorno de sucesso com mensagem.
			if (medicoSalvo != null) {
				return new ResponseEntity<>("Médico criado com sucesso", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("Falha ao criar o médico", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>("Já existe um medico cadastrado com esse nome e email", HttpStatus.CONFLICT);
		}
	}

	@CrossOrigin(origins = "http://127.0.0.1:3000")
	@PutMapping("/medico/atualizar")
	@RolesAllowed("ROLE_MEDICO")
	public ResponseEntity<String> atualizarMedico(@RequestBody MedicoRequest medicoRequest) {
		// Primeiro, verifique se o médico existe pelo nome
		Medico medicoExistente = medicoService.buscarMedicoPorNome(medicoRequest.getNome());

		if (medicoExistente == null) {
			return new ResponseEntity<>("Médico não encontrado", HttpStatus.NOT_FOUND);
		}

		// Atualize os campos do médico existente com os dados do medicoRequest
		medicoExistente.setCrm(medicoRequest.getCrm());
		medicoExistente.setCelular(medicoRequest.getCelular());
		medicoExistente.setIdade(medicoRequest.getIdade());
		medicoExistente.setEndereco(medicoRequest.getEndereco());
		medicoExistente.setCpf(medicoRequest.getCpf());
		medicoExistente.setEspecialidade(medicoRequest.getEspecialidade());
		medicoExistente.getUser().setEmail(medicoRequest.getEmail());
		medicoExistente.getUser().setPassword(medicoRequest.getPassword());

		// Salve as alterações no banco de dados
		medicoService.salvarMedico(medicoExistente);

		return new ResponseEntity<>("Médico atualizado com sucesso", HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://127.0.0.1:3000")
	@DeleteMapping("/medico/{crm}")
	@RolesAllowed("ROLE_MEDICO")
	public ResponseEntity<String> excluirMedico(@PathVariable("crm") BigInteger crm)
			throws MedicoNaoEncontradoException {
		try {
			medicoService.excluirMedico(crm);
			return ResponseEntity.ok("Medico excluído com sucesso");
		} catch (MedicoNaoEncontradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medico não encontrado");
		}
	}

}

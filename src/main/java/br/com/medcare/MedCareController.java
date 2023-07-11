package br.com.medcare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.medcare.model.Paciente;

import br.com.medcare.model.Medico;
import br.com.medcare.model.MedicoRequest;
import br.com.medcare.model.PacienteRequest;
import br.com.medcare.model.User;
import br.com.medcare.services.MedicoRepositoryService;
import br.com.medcare.services.PacienteService;
import br.com.medcare.services.RoleRepositoryService;
import br.com.medcare.services.UserRepositoryService;
import jakarta.annotation.security.RolesAllowed;

@RestController
public class MedCareController {
	
	@Autowired
	MedicoRepositoryService medicoService;
	
	@Autowired
	UserRepositoryService userService;
	
	@Autowired
	RoleRepositoryService roleService;
	
	@Autowired
	PacienteService pacienteService;
	
	@GetMapping("user")
	@RolesAllowed("ROLE_MEDICO")
	public String helloUser() {
		return "Hello User";
	}

	@GetMapping("admin")
	@RolesAllowed("ROLE_PACIENTE")
	public String helloAdmin() {
		return "Hello Admin";
	}
	
	 @PostMapping("/medicos")
	    public ResponseEntity<String> cadastraMedico(@RequestBody MedicoRequest medico) {
	        // esse endpoint seria aberto pra cadastrar um usuário médico no banco
		    roleService.Salva();
		  User usuarioSalvo =  userService.salvaMedico(medico.getEmail(), medico.getPassword());
		  Medico novomedico = new Medico();
		  novomedico.setCelular(medico.getCelular());
		  novomedico.setCpf(medico.getCpf());
		  novomedico.setCrm(medico.getCrm());
		  novomedico.setEndereco(medico.getEndereco());
		  novomedico.setEspecialidade(medico.getEspecialidade());
		  novomedico.setIdade(medico.getIdade());
		  novomedico.setUser(usuarioSalvo);
		  novomedico.setNome(medico.getNome());
		 	Medico medicoSalvo = medicoService.salvaMedico(novomedico);
	        // Exemplo de retorno de sucesso com mensagem.
		 	if (medicoSalvo != null) {
	            return new ResponseEntity<>("Médico criado com sucesso", HttpStatus.CREATED);
	        } else {
	            return new ResponseEntity<>("Falha ao criar o médico", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	 
	 
	 @PostMapping("/paciente")
	    public ResponseEntity<String> cadastraPaciente(@RequestBody PacienteRequest paciente) {
	        // esse endpoint seria aberto pra cadastrar um usuário médico no banco
		    roleService.Salva();
		  User usuarioSalvo =  userService.salvaPaciente(paciente.getEmail(), paciente.getPassword());
		  Paciente novoPaciente = new Paciente();
		  novoPaciente.setTelefone(paciente.getCelular());
		  novoPaciente.setCpf(paciente.getCpf());
		  novoPaciente.setEndereco(paciente.getEndereco());
		  novoPaciente.setIdade(paciente.getIdade());
		  novoPaciente.setUser(usuarioSalvo);
		  novoPaciente.setNome(paciente.getNome());
		 	Paciente pacienteSalvo = pacienteService.salvaPaciente(novoPaciente);
	        // Exemplo de retorno de sucesso com mensagem.
		 	if (pacienteSalvo != null) {
	            return new ResponseEntity<>("Paciente criado com sucesso", HttpStatus.CREATED);
	        } else {
	            return new ResponseEntity<>("Falha ao criar o paciente", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

}

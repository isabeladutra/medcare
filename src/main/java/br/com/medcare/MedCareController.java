package br.com.medcare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.medcare.model.Medico;
import br.com.medcare.model.MedicoRequest;
import br.com.medcare.model.User;
import br.com.medcare.services.MedicoRepositoryService;
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
		  User usuarioSalvo =  userService.Salva(medico.getEmail(), medico.getPassword());
		  Medico novomedico = new Medico();
		  novomedico.setCelular(medico.getCelular());
		  novomedico.setCpf(medico.getCpf());
		  novomedico.setCrm(medico.getCrm());
		  novomedico.setEndereco(medico.getEndereco());
		  novomedico.setEspecialidade(medico.getEspecialidade());
		  novomedico.setIdade(medico.getIdade());
		  novomedico.setUser(usuarioSalvo);
		 	Medico medicoSalvo = medicoService.salvaMedico(novomedico);
	        // Exemplo de retorno de sucesso com mensagem.
		 	if (medicoSalvo != null) {
	            return new ResponseEntity<>("Médico criado com sucesso", HttpStatus.CREATED);
	        } else {
	            return new ResponseEntity<>("Falha ao criar o médico", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

}

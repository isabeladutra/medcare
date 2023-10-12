package br.com.medcare;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.medcare.exceptions.MedicoNaoEncontradoException;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.model.ReceitaMedica;
import br.com.medcare.model.ReceitaMedicaRequest;
import br.com.medcare.services.ReceitaMedicaRepository;
import br.com.medcare.services.ReceitaMedicaService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.security.RolesAllowed;
import java.text.SimpleDateFormat;


@RequestMapping("/receitas-medicas")
@RestController
public class ReceitaMedicaController {
	
	@Autowired
	ReceitaMedicaService receitaService;
	
	public void setReceitaService(ReceitaMedicaService receitaService) {
		this.receitaService = receitaService;
	}


	public void setRepo(ReceitaMedicaRepository repo) {
		this.repo = repo;
	}


	@Autowired
	ReceitaMedicaRepository repo;
	

	
    @PostMapping("/incluir")
    @RolesAllowed("ROLE_MEDICO")
    public ResponseEntity<String> incluirReceitaMedica(@RequestBody ReceitaMedicaRequest receitaMedicaRequest) {
    	boolean receitaIncluidaComSucesso;
    	try {
    	receitaIncluidaComSucesso = receitaService.incluirReceitaMedica(receitaMedicaRequest);

    	}catch ( PacienteNaoEncontradoException e) {
			receitaIncluidaComSucesso = false;
		}catch (MedicoNaoEncontradoException ex) {
			receitaIncluidaComSucesso = false;
		}
    	
        if (receitaIncluidaComSucesso) {
            return new ResponseEntity<>("Receita médica incluída com sucesso", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Falha ao incluir a receita médica", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
	
    @GetMapping("/buscar")
    @RolesAllowed("ROLE_PACIENTE")
    public ResponseEntity<ReceitaMedica> buscarReceitaMedicaPorNomePacienteEMedico(
            @RequestParam String nomePaciente, @RequestParam String nomeMedico) {
        
        ReceitaMedica receitaMedica = receitaService.buscarReceitaPorNomePacienteENomeMedico(nomePaciente, nomeMedico);

        if (receitaMedica != null) {
            return new ResponseEntity<>(receitaMedica, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
	
    @DeleteMapping("/excluir")
    @RolesAllowed("ROLE_MEDICO")
    public ResponseEntity<String> excluirReceitaMedica(
            @RequestParam String nomePaciente,
            @RequestParam String nomeMedico,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataReceita) {

        ReceitaMedica receitaMedica = repo.findByPacienteNomeAndMedicoNomeAndDataReceita(nomePaciente, nomeMedico, dataReceita);

        if (receitaMedica != null) {
            repo.delete(receitaMedica);
            return ResponseEntity.ok("Receita médica excluída com sucesso");
        } else {
            return ResponseEntity.status(404).body("Receita médica não encontrada");
        }
    }
    
    
}

package br.com.medcare;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.medcare.mappers.ProblemasDeSaudeMapper;
import br.com.medcare.model.FichaMedica;
import br.com.medcare.model.FichaMedicaRequest;
import br.com.medcare.model.Paciente;
import br.com.medcare.services.FichaMedicaService;
import br.com.medcare.services.PacienteService;
import jakarta.annotation.security.RolesAllowed;


@RestController
@RequestMapping("/fichas-medicas")
public class FichaMedicaController {
	
	@Autowired
	PacienteService pacienteService;
	
	@Autowired
	public FichaMedicaService fichaService;

	
	@PreAuthorize("hasAnyRole('ROLE_MEDICO', 'ROLE_PACIENTE')")
    @PostMapping("/incluir")
    public ResponseEntity<String> incluirFichaMedica(@RequestBody FichaMedicaRequest fichaMedicaRequest) {
        // Primeiro, buscamos o paciente pelo ID passado no FichaMedicaRequest
        Paciente paciente = pacienteService.buscarPacientePorNome(fichaMedicaRequest.getNome());

        // Verificamos se o paciente existe
        if (paciente == null) {
            return new ResponseEntity<>("Paciente não encontrado", HttpStatus.NOT_FOUND);
        }
        // verifica se paciente já tem ficha médica
        
        FichaMedica ficha =   fichaService.buscarFichaMedicaPorNomePaciente(paciente.getNome());
        if(ficha == null) {
            // Criamos a nova ficha médica com base no FichaMedicaRequest e associamos ao paciente
            FichaMedica fichaMedica = new FichaMedica();
            fichaMedica.setNomePaciente(paciente.getNome());
            fichaMedica.setIdade(paciente.getIdade());
            fichaMedica.setAlergias(fichaMedicaRequest.getAlergias());
            fichaMedica.setAltura(fichaMedicaRequest.getAltura());
            fichaMedica.setPeso(fichaMedicaRequest.getPeso());
            fichaMedica.setContatoDeEmergencia(fichaMedicaRequest.getContatoDeEmergencia());
            fichaMedica.setPaciente(paciente);
            fichaMedica.setProblemasDeSaude(ProblemasDeSaudeMapper.mapper(fichaMedicaRequest.getProblemasDeSaude()));
            fichaMedica.setDataRegistro(LocalDateTime.now());
            // Outros campos da ficha médica

            // Salvar a ficha médica no banco de dados através do serviço
            fichaService.salvarFichaMedica(fichaMedica);

            return new ResponseEntity<>("Ficha médica incluída com sucesso", HttpStatus.CREATED);
        } else {
        	return new ResponseEntity<>("Paciente já possui ficha médica cadastrada!", HttpStatus.CONFLICT);
        }
        
        
        
        
        

    }
    
	
    @GetMapping("/buscar-por-nome/{nomePaciente}")
    @PreAuthorize("hasAnyRole('ROLE_MEDICO', 'ROLE_PACIENTE')")
    public ResponseEntity<FichaMedica> buscarFichaMedicaPorNome(@PathVariable String nomePaciente) {
        FichaMedica fichaMedica = fichaService.buscarFichaMedicaPorNomePaciente(nomePaciente);

        if (fichaMedica != null) {
            return new ResponseEntity<>(fichaMedica, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
	
    @DeleteMapping("/excluir-por-nome/{nomePaciente}")
    @PreAuthorize("hasAnyRole('ROLE_MEDICO', 'ROLE_PACIENTE')")
    public ResponseEntity<String> excluirFichaMedicaPorNome(@PathVariable String nomePaciente) {
        boolean fichaMedicaExcluida = fichaService.excluirFichaMedicaPorNomePaciente(nomePaciente);

        if (fichaMedicaExcluida) {
            return new ResponseEntity<>("Ficha médica excluída com sucesso", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ficha médica não encontrada", HttpStatus.NOT_FOUND);
        }
    }
    
    
	
    @PutMapping("/atualizar-por-nome/{nomePaciente}")
    @RolesAllowed("ROLE_PACIENTE")
    public ResponseEntity<FichaMedica> atualizarFichaMedicaPorNome(@PathVariable String nomePaciente, @RequestBody FichaMedicaRequest fichaMedicaRequest) {
        FichaMedica fichaMedicaAtualizada = fichaService.atualizarFichaMedicaPorNomePaciente(nomePaciente, fichaMedicaRequest);

        if (fichaMedicaAtualizada != null) {
            return new ResponseEntity<>(fichaMedicaAtualizada, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

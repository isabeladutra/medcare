package br.com.medcare;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.medcare.dto.ConsultaDTO;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.mappers.ConsultaDTOMapper;
import br.com.medcare.model.Consulta;
import br.com.medcare.model.Paciente;
import br.com.medcare.services.ConsultaService;
import br.com.medcare.services.PacienteService;
import jakarta.annotation.security.RolesAllowed;

@RestController @RequestMapping("paciente")
public class PacienteController {
	
	@Autowired
    private PacienteService pacienteService;

    @Autowired
    private ConsultaService consultaService;
	/*@PostMapping(value="/consultas")
	public void agendarConsulta() {
		
	}*/
	 @GetMapping("/{cpf}/consultas")
	 @RolesAllowed("ROLE_PACIENTE")
	    public ResponseEntity<?> listarConsultasDoPaciente(@PathVariable("cpf") BigInteger cpf) {
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
	    public ResponseEntity<String> excluirPaciente(@PathVariable("cpf") BigInteger cpf) {
	        try {
	            pacienteService.excluirPaciente(cpf);
	            return ResponseEntity.ok("Paciente excluído com sucesso");
	        } catch (PacienteNaoEncontradoException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não encontrado");
	        }
	    }
	 
	


	 

}

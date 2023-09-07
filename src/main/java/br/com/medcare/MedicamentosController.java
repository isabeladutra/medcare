package br.com.medcare;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.medcare.dto.ListaMedicamentoResponse;
import br.com.medcare.dto.MedicamentoRequest;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.model.Medicamentos;
import br.com.medcare.model.PrescricaoMedicamento;
import br.com.medcare.services.MedicamentosService;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentosController {

	@Autowired
	MedicamentosService service;

	@RolesAllowed("ROLE_MEDICO")
	@PostMapping("/salvar")
	public ResponseEntity<String> salvarMedicamentos(@RequestBody MedicamentoRequest dto) {
		try {
			service.salvarMedicamentos(dto);
			return ResponseEntity.ok("Medicamentos salvos com sucesso.");
		} catch (PacienteNaoEncontradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não está cadastrado no sistema");
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_MEDICO', 'ROLE_PACIENTE')")
	@GetMapping("/listar")
	public ResponseEntity<?> listarMedicamentosPorNomePaciente(@RequestParam String nomePaciente) {
		try {

			List<Medicamentos> listaMedicamentos = service.listarMedicamentosPorNomePaciente(nomePaciente);

			if (listaMedicamentos != null && !listaMedicamentos.isEmpty()) {

				ListaMedicamentoResponse medResp = new ListaMedicamentoResponse();
				List<PrescricaoMedicamento> listaPrescricao = new ArrayList<>();
				for (Medicamentos medicamentos : listaMedicamentos) {
					List<PrescricaoMedicamento> listaPrec = medicamentos.getPrescricoes();
					for (PrescricaoMedicamento prescricaoMedicamento : listaPrec) {
						listaPrescricao.add(prescricaoMedicamento);
					}
					// medResp.setListaPrescricao(medicamentos.getPrescricoes());
				}
				medResp.setListaPrescricao(listaPrescricao);
				medResp.setPaciente(listaMedicamentos.get(0).getPaciente());
				return ResponseEntity.ok(medResp);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (PacienteNaoEncontradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não está cadastrado no sistema");
		}
	}

	
	  @RolesAllowed("ROLE_MEDICO")
	  @DeleteMapping("/remover") public ResponseEntity<String> removerMedicamento( 
	  @RequestParam("nomePaciente") String nomePaciente, @RequestParam("nomeMedicamento") String nomeMedicamento ) {
		 
		  String retorno = "";
		  try {
			 retorno = service.deletarmedicamento(nomePaciente, nomeMedicamento);
		} catch (PacienteNaoEncontradoException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não está cadastrado no sistema");
		}
		  
		  if(retorno.equals("Medicamento removido com sucesso.")) {
		  return ResponseEntity.ok(retorno);
		  }
		  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(retorno);
		  
		  
	  }
	  
	  
	  @RolesAllowed("ROLE_MEDICO")
	  @PutMapping("/atualizar")
	    public ResponseEntity<String> atualizarMedicamento(
	        @RequestParam("nomePaciente") String nomePaciente,
	        @RequestParam("nomeMedicamentoAntigo") String nomeMedicamentoAntigo,
	        @RequestParam(required = false, value =  "novoNomeMedicamento") String novoNomeMedicamento,
	        @RequestParam(required = false, value = "novaDataPrescricao") String novaDataPrescricao,
	        @RequestParam(required = false, value = "novaQuantidade") int novaQuantidade
	    ) {
	        String resultado = service.atualizarMedicamento(
	            nomePaciente, 
	            nomeMedicamentoAntigo, 
	            novoNomeMedicamento, 
	            novaDataPrescricao, 
	            novaQuantidade
	        );

	        if (resultado.equals("Medicamento atualizado com sucesso.")) {
	            return ResponseEntity.ok(resultado);
	        } else {
	            return ResponseEntity.badRequest().body(resultado);
	        }
	    }
	}


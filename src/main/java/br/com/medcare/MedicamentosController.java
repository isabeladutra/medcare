package br.com.medcare;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
					//medResp.setListaPrescricao(medicamentos.getPrescricoes());
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

}

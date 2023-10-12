package br.com.medcare;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import br.com.medcare.dto.MedicamentoRequest.PrescricaoRequest;
import br.com.medcare.exceptions.MedicamentoNaoEncontradoException;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.model.Medicamentos;
import br.com.medcare.model.Paciente;
import br.com.medcare.model.PrescricaoMedicamento;
import br.com.medcare.services.MedicamentosService;
import br.com.medcare.services.PacienteService;
import br.com.medcare.services.PrescricaoMedicamentoRepository;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentosController {

	@Autowired
	MedicamentosService service;

	public void setService(MedicamentosService service) {
		this.service = service;
	}

	public void setPacService(PacienteService pacService) {
		this.pacService = pacService;
	}

	public void setPrescRepo(PrescricaoMedicamentoRepository prescRepo) {
		this.prescRepo = prescRepo;
	}


	@Autowired
	PacienteService pacService;

	@Autowired
	PrescricaoMedicamentoRepository prescRepo;

	@RolesAllowed("ROLE_MEDICO")
	@PostMapping("/salvar")
	public ResponseEntity<String> salvarMedicamentos(@RequestBody MedicamentoRequest dto) {
		String nomeDoPac = dto.getNome();
		List<Medicamentos> lista = new ArrayList<>();
		try {
			lista = service.listarMedicamentosPorNomePaciente(nomeDoPac);
		} catch (PacienteNaoEncontradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não está cadastrado no sistema");
		}
		List<PrescricaoRequest> listaPrescReq = dto.getPrescricoes();
		for (PrescricaoRequest prescricaoRequest : listaPrescReq) {
			for (Medicamentos medicamentos : lista) {
				List<PrescricaoMedicamento> listaDePrescricaoMed = medicamentos.getPrescricoes();
				for (PrescricaoMedicamento presc : listaDePrescricaoMed) {
					if (presc.getNomeMedicamento().equals(prescricaoRequest.getNomeMedicamento())) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body("O medicamento: " + prescricaoRequest.getNomeMedicamento()
										+ " já está na lista de medicamentos do paciente.");
					}

				}

			}
		}

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
	@DeleteMapping("/remover")
	public ResponseEntity<String> removerMedicamento(@RequestParam("nomePaciente") String nomePaciente,
			@RequestParam("nomeMedicamento") String nomeMedicamento) {

		String retorno = "";
		try {
			retorno = service.deletarmedicamento(nomePaciente, nomeMedicamento);
		} catch (PacienteNaoEncontradoException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não está cadastrado no sistema");
		}

		if (retorno.equals("Medicamento removido com sucesso.")) {
			return ResponseEntity.ok(retorno);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(retorno);

	}

	@RolesAllowed("ROLE_MEDICO")
	@PutMapping("/atualizar")
	public ResponseEntity<String> atualizarMedicamento(@RequestParam("nomePaciente") String nomePaciente,
			@RequestParam("nomeMedicamentoAntigo") String nomeMedicamentoAntigo,
			@RequestParam(required = false, value = "novoNomeMedicamento") String novoNomeMedicamento,
			@RequestParam(required = false, value = "novaDataPrescricao") String novaDataPrescricao,
			@RequestParam(required = false, value = "novaQuantidade") int novaQuantidade) {
		List<Medicamentos> lista = new ArrayList<>();
		try {
			lista = service.listarMedicamentosPorNomePaciente(nomePaciente);
		} catch (PacienteNaoEncontradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não está cadastrado no sistema");
		}

		for (Medicamentos medicamentos : lista) {
			List<PrescricaoMedicamento> listaDePrescricaoMed = medicamentos.getPrescricoes();
			for (PrescricaoMedicamento presc : listaDePrescricaoMed) {
				if (presc.getNomeMedicamento().equals(novoNomeMedicamento)) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
							"O medicamento: " + novoNomeMedicamento + " já está na lista de medicamentos do paciente.");

				}

			}
		}

			String resultado = service.atualizarMedicamento(nomePaciente, nomeMedicamentoAntigo, novoNomeMedicamento,
					novaDataPrescricao, novaQuantidade);

			if (resultado.equals("Medicamento atualizado com sucesso.")) {
				return ResponseEntity.ok(resultado);
			} else {
				return ResponseEntity.badRequest().body(resultado);
			}
		}
	

	@RolesAllowed("ROLE_MEDICO")
	@PutMapping("/atualizarPorId")
	public ResponseEntity<String> atualizarPrescricaoMedicamentoPorId(@RequestParam("id") Integer prescricaoId,
			@RequestParam(required = false, value = "novoNomeMedicamento") String novoNomeMedicamento,
			@RequestParam(required = false, value = "novaQuantidade") Integer novaQuantidade,
			@RequestParam("nomePaciente") String nomePaciente) {

		try {
			// Buscar o paciente pelo nome
			Paciente paciente = pacService.buscarPacientePorNome(nomePaciente);

			if (paciente != null) {

				// Buscar a lista de medicamentos do paciente
				List<Medicamentos> medicamentosDoPaciente = service.listarMedicamentosPorNomePaciente(nomePaciente);

				// Encontrar a prescrição de medicamento pelo ID na lista de medicamentos do
				// paciente
				for (Medicamentos medicamento : medicamentosDoPaciente) {
					Optional<PrescricaoMedicamento> prescricaoOptional = medicamento.getPrescricoes().stream()
							.filter(p -> p.getId().equals(prescricaoId.longValue())).findFirst();

					if (prescricaoOptional.isPresent()) {
						PrescricaoMedicamento prescricao = prescricaoOptional.get();

						// Verificar e atualizar o novo nome do medicamento, se fornecido
						if (novoNomeMedicamento != null) {
							prescricao.setNomeMedicamento(novoNomeMedicamento);
						}

						// Verificar e atualizar a nova quantidade, se fornecida
						if (novaQuantidade != null) {
							prescricao.setQuantidade(novaQuantidade);
						}

						// Atualizar a prescrição de medicamento no banco de dados
						prescRepo.save(prescricao);

						return ResponseEntity.ok("Prescrição de medicamento atualizada com sucesso");
					}
				}

				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Prescrição de medicamento com ID "
						+ prescricaoId + " não encontrada para o paciente " + nomePaciente);
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Paciente com nome " + nomePaciente + " não encontrado");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao atualizar a prescrição de medicamento");
		}
	}

}

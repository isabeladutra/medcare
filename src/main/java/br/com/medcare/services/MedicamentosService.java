package br.com.medcare.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.medcare.dto.MedicamentoRequest;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.mappers.MedicamentoMapper;
import br.com.medcare.model.Medicamentos;
import br.com.medcare.model.Paciente;
import br.com.medcare.model.PrescricaoMedicamento;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class MedicamentosService {

	@Autowired
	private MedicamentosRepository repo;

	@Autowired
	private PacienteRepository pacienteRepository;

	public void salvarMedicamentos(MedicamentoRequest dto) throws PacienteNaoEncontradoException {
		Paciente paciente = pacienteRepository.findByNome(dto.getNome());

		if (paciente != null) {
			Medicamentos medicamentos = new Medicamentos();
			Medicamentos med = new MedicamentoMapper().mapToMedicamentos(dto, paciente);

			repo.save(med);
		} else {
			throw new PacienteNaoEncontradoException("Paciente não encontrado.");
		}
	}

	public List<Medicamentos> listarMedicamentosPorNomePaciente(String nomePaciente)
			throws PacienteNaoEncontradoException {
		Paciente paciente = pacienteRepository.findByNome(nomePaciente);

		if (paciente == null) {
			throw new PacienteNaoEncontradoException("Paciente não encontrado.");
		}

		List<Medicamentos> medicamentos = repo.findByPaciente(paciente);

		if (medicamentos != null) {

			return medicamentos;
		} else {
			return Collections.emptyList();
		}
	}

	public String deletarmedicamento(String nomePaciente, String nomeMedicamento)
			throws PacienteNaoEncontradoException {
		// Primeiro, encontre o paciente pelo nome
		Paciente paciente = pacienteRepository.findByNome(nomePaciente);

		if (paciente == null) {
			throw new PacienteNaoEncontradoException("Paciente não encontrado.");
		}
		List<Medicamentos> medicamentos = repo.findByPaciente(paciente);
		String retorno = "";

		if (medicamentos != null && !medicamentos.isEmpty()) {
			PrescricaoMedicamento medicamentoParaRemover = null;
			for (Medicamentos medicamento : medicamentos) {
				List<PrescricaoMedicamento> listaPrec = medicamento.getPrescricoes();
				for (PrescricaoMedicamento prescricaoMedicamento : listaPrec) {
					if (prescricaoMedicamento.getNomeMedicamento().equals(nomeMedicamento)) {
						medicamentoParaRemover = prescricaoMedicamento;
						break;
					}

				}
				if (medicamentoParaRemover != null) {
					listaPrec.remove(medicamentoParaRemover);
					repo.save(medicamento);
					retorno = "Medicamento removido com sucesso.";

				} else {
					retorno = "Medicamento não encontrado na lista de prescrições do paciente.";
				}

			}
		} else {
			retorno = "Lista de medicamentos não encontrada para o paciente.";

		}
		return retorno;
	}
	
	public String atualizarMedicamento(
	        String nomePaciente,
	        String nomeMedicamentoAntigo,
	        String novoNomeMedicamento,
	        String novaDataPrescricao,
	        int novaQuantidade
	    ) {
	        Paciente paciente = pacienteRepository.findByNome(nomePaciente);

	        if (paciente == null) {
	            return "Paciente não encontrado.";
	        }

	        List<Medicamentos> medicamentos = repo.findByPaciente(paciente);

	        if (medicamentos != null && !medicamentos.isEmpty()) {
	        	
	        	for (Medicamentos medicamento : medicamentos) {
	            List<PrescricaoMedicamento> prescricoes = medicamento.getPrescricoes();

	            for (PrescricaoMedicamento prescricao : prescricoes) {
	                if (prescricao.getNomeMedicamento().equals(nomeMedicamentoAntigo)) {
	                    // Atualize as informações do medicamento
	                    if (novoNomeMedicamento != null) {
	                        prescricao.setNomeMedicamento(novoNomeMedicamento);
	                    }
	                    if (novaDataPrescricao != null) {
	                    	
	                    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	                    	 LocalDate dataPrescricao = LocalDate.parse(novaDataPrescricao, formatter);
	                        // Lógica para converter novaDataPrescricao para LocalDate, se necessário
	                        prescricao.setDataPrescricao(dataPrescricao);
	                    }
	                    if (novaQuantidade >= 0) {
	                        prescricao.setQuantidade(novaQuantidade);
	                    }

	                    repo.save(medicamento); // Salvar as alterações
	                    return "Medicamento atualizado com sucesso.";
	                }
	            }
	        	}

	            return "Medicamento não encontrado na lista de prescrições do paciente.";
	        } else {
	            return "Lista de medicamentos não encontrada para o paciente.";
	        }
	    }
	}



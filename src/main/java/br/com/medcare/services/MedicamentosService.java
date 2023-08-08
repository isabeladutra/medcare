package br.com.medcare.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.medcare.dto.MedicamentoRequest;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.model.Medicamentos;
import br.com.medcare.model.Paciente;
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
        	medicamentos.setListaMedicamentos(dto.getListaMedicamentos());  // Set your list of medications here
        	medicamentos.setPaciente(paciente);

            repo.save(medicamentos);
        } else {
            throw new PacienteNaoEncontradoException("Paciente não encontrado.");
        }
    }

    public List<String> listarMedicamentosPorNomePaciente(String nomePaciente) throws PacienteNaoEncontradoException {
        Paciente paciente = pacienteRepository.findByNome(nomePaciente);

        if (paciente == null) {
            throw new PacienteNaoEncontradoException("Paciente não encontrado.");
        }

        Medicamentos medicamentos = repo.findByPaciente(paciente);
        
        if (medicamentos != null) {
            return medicamentos.getListaMedicamentos();
        } else {
            return Collections.emptyList();
        }
    }

}

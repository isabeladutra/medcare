package br.com.medcare.services;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.model.Paciente;

@Service
public class PacienteService {

	@Autowired
	PacienteRepository repo;
	
	public Paciente salvarPaciente(Paciente paciente) {
		return repo.save(paciente);
		
	}
	
	public Paciente buscarPorCPF(String cpf) throws PacienteNaoEncontradoException {
		Optional<Paciente> optionalPaciente = repo.findByCpf(cpf);
		
		if (optionalPaciente.isPresent()) {
			return optionalPaciente.get();
		} else {
			throw new PacienteNaoEncontradoException("Paciente com CPF " + cpf + " não encontrado");
		}
	}
	
    public void excluirPaciente(String cpf) throws PacienteNaoEncontradoException {
        Paciente paciente = buscarPorCPF(cpf);
        
        if (paciente == null) {
            throw new PacienteNaoEncontradoException("Paciente não encontrado");
        }

        repo.delete(paciente);
    }

	/*public Paciente buscarPacientePorEmail(String emailDoPacienteAutenticado) throws PacienteNaoEncontradoException {
		Optional<Paciente> optionalPaciente = repo.findByEmail(emailDoPacienteAutenticado);
		if(optionalPaciente.isPresent()) {
			return optionalPaciente.get();
		}else {
			throw new PacienteNaoEncontradoException("Paciente com email: "+ emailDoPacienteAutenticado + " não encontrado");
		}
	}*/
    
    public Paciente buscarPacientePorNome(String nomePaciente) {
        // Aqui você pode implementar validações ou lógica adicional, se necessário
        return repo.findByNome(nomePaciente);
    }

    public List<Paciente> listarTodosPacientes() {
        return repo.findAll();
    }
	
}

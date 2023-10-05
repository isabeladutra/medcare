package br.com.medcare.services;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.model.Consulta;
import br.com.medcare.model.FichaMedica;
import br.com.medcare.model.Internacao;
import br.com.medcare.model.Medicamentos;
import br.com.medcare.model.Paciente;
import br.com.medcare.model.User;

@Service
public class PacienteService {

	@Autowired
	PacienteRepository repo;
	
	@Autowired
	ConsultaRepository consultaRepo;
	
	@Autowired
	MedicamentosRepository medRepo;
	
	@Autowired
	InternacaoRepository internacaoRepo;
	
	@Autowired
	FichaMedicaRepository fichaRepo;
	
	@Autowired
	UserRepository userRepo;
	
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
        
        List<Consulta> listaDeConsulta= consultaRepo.findByPaciente(paciente);
        
        if(!listaDeConsulta.isEmpty()) {
         	 for (Consulta consulta : listaDeConsulta) {
     	        consultaRepo.delete(consulta);
     	    }
        }
        
        List<Medicamentos> listaDeMedicamentos = medRepo.findByPaciente(paciente);
        if(!listaDeMedicamentos.isEmpty()) {
      
        	 
        	 for (Medicamentos medicamento : listaDeMedicamentos) {
        	        medRepo.delete(medicamento);
        	    }
        }
        List<Internacao> listaDeinter= internacaoRepo.findByPaciente(paciente);
        if(!listaDeinter.isEmpty()) {
        	for (Internacao internacao : listaDeinter) {
				internacaoRepo.delete(internacao);
			}
        }
        
       FichaMedica ficha = fichaRepo.findByNomePaciente(paciente.getNome());
        if(ficha != null) {
        	fichaRepo.delete(ficha);
        }
        
        Optional<User> user =userRepo.findByEmail(paciente.getUser().getEmail());
        if(user.isPresent()) {
        	userRepo.delete(user.get());
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
	
    public Paciente buscarPoremail(String email) {
    	return repo.findByUserEmail(email);
    }
}

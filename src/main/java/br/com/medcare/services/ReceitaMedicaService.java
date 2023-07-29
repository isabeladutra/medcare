package br.com.medcare.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.medcare.exceptions.MedicoNaoEncontradoException;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.model.Medico;
import br.com.medcare.model.Paciente;
import br.com.medcare.model.ReceitaMedica;
import br.com.medcare.model.ReceitaMedicaRequest;

@Service
public class ReceitaMedicaService {
	
	@Autowired
	ReceitaMedicaRepository repo;
	
	@Autowired
	MedicoRepository medicoRepo;
	
	@Autowired
	PacienteRepository pacienteRepo;

	public boolean incluirReceitaMedica(ReceitaMedicaRequest receitaMedicaRequest) throws MedicoNaoEncontradoException, PacienteNaoEncontradoException {
		
		// Verificar se o médico e o paciente existem no banco de dados
        Optional<Medico> medico = medicoRepo.findByCrm(receitaMedicaRequest.getCrmMedicoEmissor());
        Optional<Paciente> paciente = pacienteRepo.findByCpf(receitaMedicaRequest.getCpfPaciente());
    	if (medico == null) {
    		throw new MedicoNaoEncontradoException("Médico com CRM " + receitaMedicaRequest.getCrmMedicoEmissor() + " não encontrado");
		}
    	
    	if(paciente == null) {
    		throw new PacienteNaoEncontradoException("Paciente com CPF " + receitaMedicaRequest.getCpfPaciente() + " não encontrado");
    	}
    	
    	// Criar uma nova instância de ReceitaMedica e preencher os campos
        ReceitaMedica receitaMedica = new ReceitaMedica();
        receitaMedica.setPrescricao(receitaMedicaRequest.getPrescricao());
        receitaMedica.setMedico(medico.get());
        receitaMedica.setPaciente(paciente.get());
        Date data = new Date();
        
        receitaMedica.setDataReceita(data);

        // Salvar a receita médica no banco de dados
        repo.save(receitaMedica);

        return true;
	}
	
	
	  public ReceitaMedica buscarReceitaPorNomePacienteENomeMedico(String nomePaciente, String nomeMedico) {
	        List<ReceitaMedica> receitasMedicas = repo.findByPacienteNomeAndMedicoNome(nomePaciente, nomeMedico);
	        
	        if (!receitasMedicas.isEmpty()) {
	            // Retorna a primeira receita encontrada (ou você pode definir outra lógica de escolha)
	            return receitasMedicas.get(0);
	        } else {
	            return null;
	        }
	    }
	  
	  public boolean excluirReceitaPorNomePacienteMedicoEData(String nomePaciente, String nomeMedico, LocalDate dataReceita) {
	        try {
	        	
	        	  ZonedDateTime zonedDateTime = dataReceita.atStartOfDay(ZoneId.systemDefault());

	              // Converte a ZonedDateTime para Instant
	              Instant instant = zonedDateTime.toInstant();

	              // Obtém o Date a partir do Instant
	              Date date = Date.from(instant);
	        	
	            repo.deleteByPacienteNomeAndMedicoNomeAndDataReceita(nomePaciente, nomeMedico, date);
	            return true;
	        } catch (Exception ex) {
	            // Tratar a exceção caso ocorra algum erro na exclusão
	            return false;
	        }
	    }
}

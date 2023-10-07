package br.com.medcare.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.medcare.model.Internacao;
import br.com.medcare.model.InternacaoRequest;
import br.com.medcare.model.Medico;
import br.com.medcare.model.Paciente;

@Service
public class InternacaoService {
	
	@Autowired
	private PacienteRepository pacienteRepository;
	
	@Autowired
	private MedicoRepository medicoRepository;
	
	@Autowired
	private InternacaoRepository internacaoRepository;
	
	public Internacao adicionarInternacao(InternacaoRequest internacaoRequest) {
        // Buscar o paciente e o médico no banco de dados pelos IDs fornecidos na requisição
        Paciente paciente = pacienteRepository.findByNome(internacaoRequest.getPacienteNome());
    

        // Verificar se o paciente e o médico foram encontrados no banco de dados
        if (paciente != null) {
            // Criar uma nova instância de Internacao e atribuir os dados recebidos da requisição
            Internacao novaInternacao = new Internacao();
            novaInternacao.setDataEntradaInternacao(internacaoRequest.getDataEntrada());
            novaInternacao.setDataSaidaInternacao(internacaoRequest.getDataSaida());
            novaInternacao.setNomeHospital(internacaoRequest.getNomeHospital());
            novaInternacao.setMotivoInternacao(internacaoRequest.getMotivoInternacao());
            novaInternacao.setPaciente(paciente);
     

            // Salvar a internação no banco de dados e retorná-la
            return internacaoRepository.save(novaInternacao);
        }

        // Caso o paciente ou o médico não sejam encontrados, retornar null
        return null;
    }
	
	public List<Internacao> buscarInternacoesPorNomePaciente(String nomePaciente) {
        // Buscar o paciente no banco de dados pelo nome
        Paciente paciente = pacienteRepository.findByNome(nomePaciente);

        // Verificar se o paciente foi encontrado
        if (paciente != null) {
            // Buscar as internações associadas ao paciente
            return internacaoRepository.findByPaciente(paciente);
        }

        // Caso o paciente não seja encontrado, retornar uma lista vazia
        return Collections.emptyList();
    }
	
	
	public Internacao atualizarInternacao(String nomePaciente, LocalDateTime dataEntradaInternacao, InternacaoRequest internacaoRequest) {
	    Paciente paciente = pacienteRepository.findByNome(nomePaciente);
	    // Buscar a internação pelo nome do paciente e data de entrada no banco de dados
	    List<Internacao> lista = internacaoRepository.findByPaciente(paciente);

	    Internacao inter = internacaoRepository.findByPacienteAndDataEntradaInternacao(paciente, dataEntradaInternacao);

	    if (inter != null) {
	        // Atualizar os dados da internação com os novos dados do internacaoRequest
	        inter.setDataEntradaInternacao(internacaoRequest.getDataEntrada());
	        inter.setDataSaidaInternacao(internacaoRequest.getDataSaida());
	        inter.setNomeHospital(internacaoRequest.getNomeHospital());
	        inter.setMotivoInternacao(internacaoRequest.getMotivoInternacao());
	        // Salvar a internação atualizada no banco de dados
	        return internacaoRepository.save(inter);
	    }

	    return null; // Caso a internação não seja encontrada, retornar null
	}

	  public boolean excluirInternacao(String nomePaciente, LocalDateTime dataEntradaInternacao) {
		  
		  Paciente paciente = pacienteRepository.findByNome(nomePaciente);
	        // Buscar a internação pelo nome do paciente e data de entrada no banco de dados
	        Internacao internacao = internacaoRepository.findByPacienteAndDataEntradaInternacao(paciente, dataEntradaInternacao);

	        if (internacao != null) {
	            internacaoRepository.delete(internacao);
	            return true;
	        }

	        return false; // Caso a internação não seja encontrada, retornar false
	    }

}










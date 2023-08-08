package br.com.medcare.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.medcare.mappers.ProblemasDeSaudeMapper;
import br.com.medcare.model.FichaMedica;
import br.com.medcare.model.FichaMedicaRequest;

@Service
public class FichaMedicaService {
	
	@Autowired
	private FichaMedicaRepository fichaMedicaRepository;
	
	 public void salvarFichaMedica(FichaMedica fichaMedica) {
	        // Aqui você pode implementar a lógica de validação, processamento ou outras operações antes de salvar a ficha médica
	        fichaMedicaRepository.save(fichaMedica);
	    }
	 
	 public FichaMedica buscarFichaMedicaPorNomePaciente(String nomePaciente) {
	        return fichaMedicaRepository.findByNomePaciente(nomePaciente);
	    }

	 public boolean excluirFichaMedicaPorNomePaciente(String nomePaciente) {
	        FichaMedica fichaMedica = fichaMedicaRepository.findByNomePaciente(nomePaciente);

	        if (fichaMedica != null) {
	            fichaMedicaRepository.delete(fichaMedica);
	            return true;
	        } else {
	            return false;
	        }
	    }
	 
	 public FichaMedica atualizarFichaMedicaPorNomePaciente(String nomePaciente, FichaMedicaRequest fichaMedicaRequest) {
	        FichaMedica fichaMedicaExistente = fichaMedicaRepository.findByNomePaciente(nomePaciente);

	        if (fichaMedicaExistente != null) {
	            // Atualiza os campos da ficha médica com base nos dados recebidos
	            fichaMedicaExistente.setIdade(fichaMedicaRequest.getIdade());
	            fichaMedicaExistente.setPeso(fichaMedicaRequest.getPeso());
	            fichaMedicaExistente.setAltura(fichaMedicaRequest.getAltura());
	            fichaMedicaExistente.setDataDeNascimento(fichaMedicaRequest.getDataDeNascimento());
	            fichaMedicaExistente.setProblemasDeSaude(ProblemasDeSaudeMapper.mapper(fichaMedicaRequest.getProblemasDeSaude()));
	            fichaMedicaExistente.setContatoDeEmergencia(fichaMedicaRequest.getContatoDeEmergencia());
	            fichaMedicaExistente.setAlergias(fichaMedicaRequest.getAlergias());

	            return fichaMedicaRepository.save(fichaMedicaExistente);
	        } else {
	            return null;
	        }
	    }
}

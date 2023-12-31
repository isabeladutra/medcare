package br.com.medcare.services;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.medcare.exceptions.MedicoNaoEncontradoException;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.model.Consulta;
import br.com.medcare.model.Internacao;
import br.com.medcare.model.Medico;
import br.com.medcare.model.Paciente;
import br.com.medcare.model.ReceitaMedica;
import br.com.medcare.model.Role;
import br.com.medcare.model.User;

@Service
public class MedicoService {

	@Autowired
	MedicoRepository repo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	InternacaoRepository interRepo;

	@Autowired
	ConsultaRepository consulRepo;

	@Autowired
	ReceitaMedicaRepository ReceitaRepo;

	public Medico salvarMedico(Medico medico) {
		return repo.save(medico);

	}

	public Medico buscarMedicoPorEmail(String email) {
		Optional<Medico> medicoOptional = repo.findByUserEmail(email);
		return medicoOptional.orElse(null);
	}

	

	public Medico buscarPorCRM(BigInteger bigInteger) throws MedicoNaoEncontradoException {
		Optional<Medico> optionalMedico = repo.findByCrm(bigInteger);

		if (optionalMedico.isPresent()) {
			return optionalMedico.get();
		} else {
			// Lidar com a situação em que o médico não é encontrado, como lançar uma
			// exceção ou retornar null
			// Exemplo de lançamento de exceção:
			throw new MedicoNaoEncontradoException("Médico com CRM " + bigInteger + " não encontrado");
		}
	}

	public void excluirMedico(BigInteger crm) throws MedicoNaoEncontradoException {
		Medico medico = buscarPorCRM(crm);

		if (medico == null) {
			throw new MedicoNaoEncontradoException("Medico não encontrado");
		}

		List<Consulta> listaConsul = consulRepo.findByMedico(medico);
		if (!listaConsul.isEmpty()) {
			for (Consulta consulta : listaConsul) {
				consulRepo.delete(consulta);
			}
		}

	

		List<ReceitaMedica> listaDeReceita = ReceitaRepo.findByMedico(medico);
		if (!listaDeReceita.isEmpty()) {
			for (ReceitaMedica receitaMedica : listaDeReceita) {
				ReceitaRepo.delete(receitaMedica);
			}
		}

		repo.delete(medico);
	}

	public Medico buscarMedicoPorNome(String nomeMedicoExistente) {
		Medico medico = repo.findByNome(nomeMedicoExistente);
		return medico;
	}

}

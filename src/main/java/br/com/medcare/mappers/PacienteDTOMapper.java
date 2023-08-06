package br.com.medcare.mappers;


import br.com.medcare.dto.PacienteDTO;
import br.com.medcare.model.Paciente;
import lombok.Data;

@Data
public class PacienteDTOMapper {
	
	public static PacienteDTO mapper(Paciente pac) {
		PacienteDTO dto = new PacienteDTO();
		dto.setCpf(pac.getCpf());
		dto.setEmail(pac.getUser().getEmail());
		dto.setEnderecoCompleto(pac.getEndereco());
		dto.setTelefone(pac.getTelefone());
		dto.setNome(pac.getNome());
		dto.setDataDeNascimento(pac.getDataDenascimento());
		return dto;
		
	}

}

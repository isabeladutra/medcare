package br.com.medcare.mappers;

import br.com.medcare.dto.ConsultaDTO;
import br.com.medcare.model.Consulta;
import lombok.Data;

@Data
public class ConsultaDTOMapper {
	
	public static ConsultaDTO mapper(Consulta consulta) {
		ConsultaDTO cons = new ConsultaDTO();
		cons.setDataHora(consulta.getDataHora());
		cons.setDescricao(cons.getDescricao());
		cons.setCrm(consulta.getMedico().getCrm());
		cons.setNomeMedico(consulta.getPaciente().getNome());
		cons.setNomePaciente(consulta.getPaciente().getNome());
		cons.setCpfPaciente(consulta.getPaciente().getCpf());
		cons.setDescricao(consulta.getDescricao());
		cons.setEspecialidade(consulta.getMedico().getEspecialidade());
		return cons;
	}

}

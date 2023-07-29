package br.com.medcare.mappers;

import br.com.medcare.model.Consulta;
import br.com.medcare.model.ConsultaRequest;
import br.com.medcare.model.Medico;
import lombok.Data;

@Data
public class ConsultaMapper {
	
	public static Consulta mapper(ConsultaRequest consultareq) {
		Consulta consulta = new Consulta();
		consulta.setDataHora(consultareq.getDataDaConsulta());
		consulta.setDescricao(consultareq.getObservacoes());
		return consulta;
		
	}

}

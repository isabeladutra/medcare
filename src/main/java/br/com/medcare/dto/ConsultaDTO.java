package br.com.medcare.dto;


import java.math.BigInteger;
import java.time.LocalDateTime;

import br.com.medcare.model.Medico;
import br.com.medcare.model.Paciente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaDTO {

	 private LocalDateTime dataHora;
	 private String descricao;
	    
	 private BigInteger crm;
	 private String especialidade;
	 private String nomeMedico;
	 private String nomePaciente;
	 private BigInteger cpfPaciente;
	    
	    
}

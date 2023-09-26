package br.com.medcare.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ConsultaRequest {
	

	private LocalDateTime dataDaConsulta;
	private String nomeDoMedico;
	private String nomeDopaciente;
	private BigInteger crmMedico;
	private String cpfpaciente;
	private String observacoes;
	

}

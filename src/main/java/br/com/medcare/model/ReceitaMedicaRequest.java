package br.com.medcare.model;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ReceitaMedicaRequest {
	
	private String nomeMedicoEmissor;
	private BigInteger crmMedicoEmissor;
	private String nomePaciente;
	private BigInteger cpfPaciente;
	private String prescricao;

}

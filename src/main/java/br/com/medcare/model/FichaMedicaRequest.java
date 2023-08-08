package br.com.medcare.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class FichaMedicaRequest {
	
	private String nome;
	private Integer idade;
	private double peso;
	private double altura;
	private String dataDeNascimento;
	private ProblemasDeSaudeRequest problemasDeSaude;
	private Integer contatoDeEmergencia;
	private String alergias;

}

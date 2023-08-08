package br.com.medcare.dto;

import java.util.List;

import lombok.Data;

@Data
public class MedicamentoRequest {
	
	private String nome;
	private List<String> listaMedicamentos;

}

package br.com.medcare.model;

import lombok.Data;

@Data
public class PacienteRequest {
	 private Integer id;
	    private Integer idade;
	    private Integer telefone;
	    private String endereco;
	    private Integer cpf;
	    private String nome;
	    private String email;	    
	    private String password;
	    private Integer celular;

}

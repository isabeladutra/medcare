package br.com.medcare.model;

import java.math.BigInteger;

import lombok.Data;

@Data
public class MedicoRequest {
	 private BigInteger crm;
	    private String celular;
	    private Integer idade;
	    private String endereco;
	    private String cpf;
	    private String especialidade;
	    private String email;	    
	    private String password;
	    private String nome;

}

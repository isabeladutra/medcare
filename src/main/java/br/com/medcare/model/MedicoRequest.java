package br.com.medcare.model;

import java.math.BigInteger;

import lombok.Data;

@Data
public class MedicoRequest {
	 private BigInteger crm;
	    private Integer celular;
	    private Integer idade;
	    private String endereco;
	    private BigInteger cpf;
	    private String especialidade;
	    private String email;	    
	    private String password;
	    private String nome;

}

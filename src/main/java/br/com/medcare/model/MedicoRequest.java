package br.com.medcare.model;

import lombok.Data;

@Data
public class MedicoRequest {
	 private Integer crm;
	    private Integer celular;
	    private Integer idade;
	    private String Endereco;
	    private Integer cpf;
	    private String especialidade;
	    private String email;	    
	    private String password;

}

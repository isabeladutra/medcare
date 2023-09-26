package br.com.medcare.model;

import java.math.BigInteger;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import lombok.Data;

@Data
public class PacienteRequest {
	 private Integer id;
	    private Integer idade;
	    private String telefone;
	    private String endereco;
	    private String cpf;
	    private String nome;
	    private String email;	    
	    private String password;
	    private String celular;
	    private String dataDeNascimento;
	    
	    
	    
	    public Date getDataDeNascimentoAsDate() throws ParseException {
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        return sdf.parse(dataDeNascimento);
	    }
	






}

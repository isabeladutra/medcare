package br.com.medcare.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacienteDTO {
	
	private String nome;
	private BigInteger cpf;
	private String enderecoCompleto;
	private String email;
	private String dataDeNascimentoFormatted;
	private Integer telefone;
	    // getters e setters

	    public void setDataDeNascimento(Date dataDeNascimento) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	        this.dataDeNascimentoFormatted = dateFormat.format(dataDeNascimento);
	    }
	

}

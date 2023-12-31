package br.com.medcare.model;

import java.math.BigInteger;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table(name = "paciente") 
public class Paciente{
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	    private Integer idade;
	    private String telefone;
	    private String endereco;
	    private String cpf;
	    private String nome;
	    private Date dataDenascimento;
	    private String celular;
	    
	    @OneToOne(cascade = CascadeType.ALL)
	    private User user;

}

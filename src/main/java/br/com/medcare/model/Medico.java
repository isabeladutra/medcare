package br.com.medcare.model;

import java.math.BigInteger;

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
@Table(name = "medico") 
public class Medico  {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	    private BigInteger crm;
	    private String celular;
	    private Integer idade;
	    private String Endereco;
	    private String cpf;
	    private String especialidade;
	    private String nome;
	    
	    @OneToOne
	    private User user;
}

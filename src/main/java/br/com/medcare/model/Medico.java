package br.com.medcare.model;

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
	    private Integer crm;
	    private Integer celular;
	    private Integer idade;
	    private String Endereco;
	    private Integer cpf;
	    private String especialidade;
	    private String nome;
	    
	    @OneToOne
	    private User user;
}

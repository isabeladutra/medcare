package br.com.medcare.model;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "receita_medica")
@NoArgsConstructor
@AllArgsConstructor
public class ReceitaMedica {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String prescricao;
	
	  @ManyToOne
	    private Medico medico;
	    
	    @ManyToOne
	    private Paciente paciente;
	    
	 @Temporal(TemporalType.DATE)
	 private Date dataReceita;
}

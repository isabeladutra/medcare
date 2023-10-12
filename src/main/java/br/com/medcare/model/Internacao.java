package br.com.medcare.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table(name = "internacao") 
public class Internacao {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	 @Column(columnDefinition = "TIMESTAMP")
	    private LocalDateTime dataEntradaInternacao;
	   
	   @Column(columnDefinition = "TIMESTAMP")
	    private LocalDateTime dataSaidaInternacao;
	    private String nomeHospital;
		private String motivoInternacao;
		
		
		@ManyToOne
		@JoinColumn(name = "paciente_id")
		private Paciente paciente;
		
			 
	
}

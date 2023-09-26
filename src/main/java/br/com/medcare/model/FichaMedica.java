package br.com.medcare.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table(name = "ficha_medica") 
public class FichaMedica {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String nomePaciente;
	    private Integer idade;
	    private LocalDateTime dataRegistro;
	    private double peso;
	    private double altura;
	    private String dataDeNascimento;
	    private String sexo;
	    
	    private ProblemasDeSaude problemasDeSaude;
	    
	    private Integer contatoDeEmergencia;
	    private String alergias;

	    @ManyToOne
	    @JoinColumn(name = "paciente_id")
	    private Paciente paciente;
	    
	    public void setDataDeNascimentoFormatted(String dataDeNascimento) {
	    	  // Use o DateTimeFormatter para fazer a formatação da data
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        LocalDate localDate = LocalDate.parse(dataDeNascimento, formatter);

	        // Atribua a data formatada ao campo dataDeNascimento (que é uma string)
	        this.dataDeNascimento = localDate.toString();
		}

}

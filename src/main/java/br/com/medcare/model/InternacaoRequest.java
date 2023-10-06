package br.com.medcare.model;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
	public class InternacaoRequest {

	   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	    private LocalDateTime dataEntrada;

	    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	    private LocalDateTime dataSaida;
	    private String nomeHospital;
	    private String motivoInternacao;
	    private String pacienteNome; // ID do paciente associado à internação
	

	}



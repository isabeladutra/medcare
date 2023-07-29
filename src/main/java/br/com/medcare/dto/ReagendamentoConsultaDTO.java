package br.com.medcare.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReagendamentoConsultaDTO {
	 private LocalDateTime dataHoraConsultaAtual;
	 private LocalDateTime novaDataHoraConsulta;

}

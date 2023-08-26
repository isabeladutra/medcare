package br.com.medcare.dto;

import java.util.List;

import br.com.medcare.model.Paciente;
import br.com.medcare.model.PrescricaoMedicamento;
import lombok.Data;

@Data
public class ListaMedicamentoResponse {
	List<PrescricaoMedicamento> listaPrescricao;
	Paciente paciente;

}

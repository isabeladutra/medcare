package br.com.medcare.dto;

import java.util.List;

import lombok.Data;

@Data
public class MedicamentoRequest {
    
    private String nome;
    private List<PrescricaoRequest> prescricoes;
    
    @Data
    public static class PrescricaoRequest {
        private String nomeMedicamento;
        private int quantidade;
        private String dataPrescricao; // Manter como String no formato "dd/MM/yyyy"
    }
}

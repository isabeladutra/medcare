package br.com.medcare.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescricaoMedicamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeMedicamento;
    private int quantidade;
    private LocalDate dataPrescricao;
    
    // Método para formatar a data como string no formato "dd/mm/yyyy"
    public String getFormattedDataPrescricao() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dataPrescricao.format(formatter);
    }
    
    // Other constructors, getters, setters, and methods
}





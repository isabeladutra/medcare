package br.com.medcare.model;

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
import br.com.medcare.model.Paciente;

import java.util.List;

import lombok.Data;

@NoArgsConstructor
@Data
@Entity 
@Table(name = "medicamentos") 
public class Medicamentos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> listaMedicamentos;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    public Medicamentos(List<String> listaMedicamentos, Paciente paciente) {
        this.listaMedicamentos = listaMedicamentos;
        this.paciente = paciente;
    }
    
    // Other constructors, getters, setters, and methods
}










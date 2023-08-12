package br.com.medcare.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity 
@Table(name = "medicamentos") 
public class Medicamentos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "medicamentos_id")
    private List<PrescricaoMedicamento> prescricoes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    public Medicamentos(Paciente paciente) {
        this.paciente = paciente;
    }
    
    // Other constructors, getters, setters, and methods
}






package br.com.medcare.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medcare.model.Medicamentos;
import br.com.medcare.model.Paciente;

public interface MedicamentosRepository extends JpaRepository<Medicamentos, Integer> {

	List<Medicamentos> findByPaciente(Paciente paciente);

}

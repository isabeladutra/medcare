package br.com.medcare.services;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medcare.model.PrescricaoMedicamento;

public interface PrescricaoMedicamentoRepository extends JpaRepository<PrescricaoMedicamento, Long>{

}

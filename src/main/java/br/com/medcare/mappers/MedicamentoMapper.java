package br.com.medcare.mappers;


import br.com.medcare.dto.MedicamentoRequest;
import br.com.medcare.dto.MedicamentoRequest.PrescricaoRequest;
import br.com.medcare.model.Medicamentos;
import br.com.medcare.model.Paciente;
import br.com.medcare.model.PrescricaoMedicamento;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MedicamentoMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Medicamentos mapToMedicamentos(MedicamentoRequest medicamentoRequest, Paciente pac) {
        Medicamentos medicamentos = new Medicamentos();

        medicamentos.setPaciente(pac);

        List<PrescricaoMedicamento> prescricoes = medicamentoRequest.getPrescricoes()
            .stream()
            .map(this::mapToPrescricaoMedicamento)
            .collect(Collectors.toList());

        medicamentos.setPrescricoes(prescricoes);

        return medicamentos;
    }

    private PrescricaoMedicamento mapToPrescricaoMedicamento(PrescricaoRequest prescricaoRequest) {
        PrescricaoMedicamento prescricaoMedicamento = new PrescricaoMedicamento();
        prescricaoMedicamento.setNomeMedicamento(prescricaoRequest.getNomeMedicamento());
        prescricaoMedicamento.setQuantidade(prescricaoRequest.getQuantidade());

        LocalDate dataPrescricao = LocalDate.parse(prescricaoRequest.getDataPrescricao(), DATE_FORMATTER);
        prescricaoMedicamento.setDataPrescricao(dataPrescricao);

        return prescricaoMedicamento;
    }
}


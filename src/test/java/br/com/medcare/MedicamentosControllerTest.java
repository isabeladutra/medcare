package br.com.medcare;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.medcare.services.MedicamentosService;
import br.com.medcare.services.PacienteService;
import br.com.medcare.services.PrescricaoMedicamentoRepository;

import java.util.ArrayList;
import java.util.List;
import br.com.medcare.model.Medicamentos;
import br.com.medcare.model.Paciente;
import br.com.medcare.model.PrescricaoMedicamento;
import br.com.medcare.dto.MedicamentoRequest;
import br.com.medcare.dto.MedicamentoRequest.PrescricaoRequest;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class MedicamentosControllerTest {

  
    private MedicamentosController medicamentosController;

   
    private MedicamentosService medicamentosService;

  
    private PacienteService pacienteService;

  
    private PrescricaoMedicamentoRepository prescricaoMedicamentoRepository;
    
    @BeforeEach
    public void setUp() {
    	medicamentosService = mock(MedicamentosService.class);
    	pacienteService = mock(PacienteService.class);
    	prescricaoMedicamentoRepository = mock(PrescricaoMedicamentoRepository.class);
    	medicamentosController = new MedicamentosController();
    	medicamentosController.setPacService(pacienteService);
    	medicamentosController.setPrescRepo(prescricaoMedicamentoRepository);
    	medicamentosController.setService(medicamentosService);
    }

    @Test
    void testSalvarMedicamentosSuccess() throws PacienteNaoEncontradoException {
        MedicamentoRequest dto = new MedicamentoRequest(/* valores de teste */);
        dto.setNome("aaa");
        List<PrescricaoRequest> lista= new ArrayList<>();
        dto.setPrescricoes(lista);
        when(pacienteService.buscarPacientePorNome(anyString())).thenReturn(new Paciente());
        when(medicamentosService.listarMedicamentosPorNomePaciente(anyString())).thenReturn(new ArrayList<>());

        ResponseEntity<String> response = medicamentosController.salvarMedicamentos(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Medicamentos salvos com sucesso.", response.getBody());
    }

    @Test
    void testSalvarMedicamentosPacienteNaoEncontrado() throws PacienteNaoEncontradoException {
        MedicamentoRequest dto = new MedicamentoRequest(/* valores de teste */);
        dto.setNome("aaa");
     
        Mockito.doThrow(new PacienteNaoEncontradoException("Paciente não encontrado"))
        .when(medicamentosService).listarMedicamentosPorNomePaciente(anyString());
        ResponseEntity<String> response = medicamentosController.salvarMedicamentos(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Paciente não está cadastrado no sistema", response.getBody());
    }

    @Test
    void testSalvarMedicamentosMedicamentoExistente() throws PacienteNaoEncontradoException {
        MedicamentoRequest dto = new MedicamentoRequest(/* valores de teste */);
        dto.setNome("aaa");
        when(pacienteService.buscarPacientePorNome(anyString())).thenReturn(new Paciente());
        List<Medicamentos> medicamentosList = new ArrayList<>();
        Medicamentos medicamentos = new Medicamentos();
        List<PrescricaoMedicamento> listaPresc = new ArrayList<>();
        PrescricaoMedicamento pres = new PrescricaoMedicamento();
        pres.setNomeMedicamento("dipirona");
        listaPresc.add(pres);
        medicamentos.setPrescricoes(listaPresc);
        List<PrescricaoRequest> listaPresReq = new ArrayList<>();
        PrescricaoRequest presreq = new PrescricaoRequest();
        presreq.setNomeMedicamento("dipirona");
        listaPresReq.add(presreq);
        dto.setPrescricoes(listaPresReq);
        medicamentosList.add(medicamentos);
        when(medicamentosService.listarMedicamentosPorNomePaciente(anyString())).thenReturn(medicamentosList);

        ResponseEntity<String> response = medicamentosController.salvarMedicamentos(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("O medicamento: " + dto.getPrescricoes().get(0).getNomeMedicamento()
                + " já está na lista de medicamentos do paciente.", response.getBody());
    }

    // Adicione mais testes conforme necessário para outros métodos do controlador
}


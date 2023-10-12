package br.com.medcare;


import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.medcare.services.ReceitaMedicaRepository;
import br.com.medcare.services.ReceitaMedicaService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.com.medcare.exceptions.MedicoNaoEncontradoException;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.model.ReceitaMedica;
import br.com.medcare.model.ReceitaMedicaRequest;

public class ReceitaMedicaControllerTest {

  
    private ReceitaMedicaService receitaMedicaService;

   
    private ReceitaMedicaRepository receitaMedicaRepository;

    
    private ReceitaMedicaController receitaMedicaController;
    
    @BeforeEach
    public void setUp() {
    	receitaMedicaController = new ReceitaMedicaController();
    	receitaMedicaRepository = mock(ReceitaMedicaRepository.class);
    	receitaMedicaService = mock(ReceitaMedicaService.class);
    	receitaMedicaController.setReceitaService(receitaMedicaService);
    	receitaMedicaController.setRepo(receitaMedicaRepository);
    }

    @Test
    void testIncluirReceitaMedicaSucesso() throws MedicoNaoEncontradoException, PacienteNaoEncontradoException {
        ReceitaMedicaRequest request = new ReceitaMedicaRequest(/* preencha com os dados necessários */);
        when(receitaMedicaService.incluirReceitaMedica(any(ReceitaMedicaRequest.class))).thenReturn(true);

        ResponseEntity<String> response = receitaMedicaController.incluirReceitaMedica(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Receita médica incluída com sucesso", response.getBody());
    }

    @Test
    void testIncluirReceitaMedicaFalha() throws MedicoNaoEncontradoException, PacienteNaoEncontradoException {
        ReceitaMedicaRequest request = new ReceitaMedicaRequest(/* preencha com os dados necessários */);
        when(receitaMedicaService.incluirReceitaMedica(any(ReceitaMedicaRequest.class))).thenReturn(false);

        ResponseEntity<String> response = receitaMedicaController.incluirReceitaMedica(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Falha ao incluir a receita médica", response.getBody());
    }

    @Test
    void testBuscarReceitaMedicaExistente() {
        String nomePaciente = "Nome do Paciente";
        String nomeMedico = "Nome do Médico";
        Date dataReceita = new Date(); // Preencha com a data desejada
        ReceitaMedica receitaMedica = new ReceitaMedica(/* preencha com os dados necessários */);
        when(receitaMedicaService.buscarReceitaPorNomePacienteENomeMedico(nomePaciente, nomeMedico)).thenReturn(receitaMedica);

        ResponseEntity<ReceitaMedica> response = receitaMedicaController.buscarReceitaMedicaPorNomePacienteEMedico(nomePaciente, nomeMedico);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(receitaMedica, response.getBody());
    }

    @Test
    void testBuscarReceitaMedicaNaoEncontrada() {
        String nomePaciente = "Nome do Paciente";
        String nomeMedico = "Nome do Médico";
        when(receitaMedicaService.buscarReceitaPorNomePacienteENomeMedico(nomePaciente, nomeMedico)).thenReturn(null);

        ResponseEntity<ReceitaMedica> response = receitaMedicaController.buscarReceitaMedicaPorNomePacienteEMedico(nomePaciente, nomeMedico);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testExcluirReceitaMedicaExistente() {
        String nomePaciente = "Nome do Paciente";
        String nomeMedico = "Nome do Médico";
        Date dataReceita = new Date(); // Preencha com a data desejada
        ReceitaMedica receitaMedica = new ReceitaMedica(/* preencha com os dados necessários */);
        when(receitaMedicaRepository.findByPacienteNomeAndMedicoNomeAndDataReceita(nomePaciente, nomeMedico, dataReceita)).thenReturn(receitaMedica);

        ResponseEntity<String> response = receitaMedicaController.excluirReceitaMedica(nomePaciente, nomeMedico, dataReceita);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Receita médica excluída com sucesso", response.getBody());
    }

    @Test
    void testExcluirReceitaMedicaNaoEncontrada() {
        String nomePaciente = "Nome do Paciente";
        String nomeMedico = "Nome do Médico";
        Date dataReceita = new Date(); // Preencha com a data desejada
        when(receitaMedicaRepository.findByPacienteNomeAndMedicoNomeAndDataReceita(nomePaciente, nomeMedico, dataReceita)).thenReturn(null);

        ResponseEntity<String> response = receitaMedicaController.excluirReceitaMedica(nomePaciente, nomeMedico, dataReceita);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Receita médica não encontrada", response.getBody());
    }
}

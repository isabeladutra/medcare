package br.com.medcare;

import br.com.medcare.model.FichaMedica;
import br.com.medcare.model.FichaMedicaRequest;
import br.com.medcare.services.FichaMedicaService;
import br.com.medcare.services.PacienteService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import br.com.medcare.model.Paciente;
import br.com.medcare.model.ProblemasDeSaudeRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class FichaMedicaControllerTest {

    private PacienteService pacienteServiceMock;
    private FichaMedicaService fichaMedicaServiceMock;
    private FichaMedicaController fichaMedicaController;

    @BeforeEach
    public void setUp() {
        pacienteServiceMock = mock(PacienteService.class);
        fichaMedicaServiceMock = mock(FichaMedicaService.class);
        fichaMedicaController = new FichaMedicaController();
        fichaMedicaController.setPacienteService(pacienteServiceMock);
        fichaMedicaController.setFichaService(fichaMedicaServiceMock);
    }

    @Test
    public void testIncluirFichaMedicaSuccess() {
        // Arrange
        FichaMedicaRequest fichaMedicaRequest = new FichaMedicaRequest(/* valores de teste */);
        Paciente paciente = new Paciente(/* valores de teste */);
        paciente.setNome("aaa");
        fichaMedicaRequest.setNome("aaa");
        ProblemasDeSaudeRequest pro = new ProblemasDeSaudeRequest();
        List<String> lista = new ArrayList<>();
        pro.setDoencasCongenitas(lista);
        pro.setDoencasCronicas(lista);
        fichaMedicaRequest.setProblemasDeSaude(pro);
        fichaMedicaRequest.setDataDeNascimento("22/08/1995");
        when(pacienteServiceMock.buscarPacientePorNome(anyString())).thenReturn(paciente);
        when(fichaMedicaServiceMock.buscarFichaMedicaPorNomePaciente(anyString())).thenReturn(null);
        
        // Act
        ResponseEntity<String> response = fichaMedicaController.incluirFichaMedica(fichaMedicaRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Ficha médica incluída com sucesso", response.getBody());
    }

    @Test
    public void testIncluirFichaMedicaConflict() {
        // Arrange
        FichaMedicaRequest fichaMedicaRequest = new FichaMedicaRequest(/* valores de teste */);
        Paciente paciente = new Paciente(/* valores de teste */);
        paciente.setNome("aaa");
        fichaMedicaRequest.setNome("aaa");
        ProblemasDeSaudeRequest pro = new ProblemasDeSaudeRequest();
        List<String> lista = new ArrayList<>();
        pro.setDoencasCongenitas(lista);
        pro.setDoencasCronicas(lista);
        fichaMedicaRequest.setProblemasDeSaude(pro);
        fichaMedicaRequest.setDataDeNascimento("22/08/1995");
        FichaMedica ficha = new FichaMedica();
        when(pacienteServiceMock.buscarPacientePorNome("aaa")).thenReturn(paciente);
        when(fichaMedicaServiceMock.buscarFichaMedicaPorNomePaciente(anyString())).thenReturn(ficha);

        // Act
        ResponseEntity<String> response = fichaMedicaController.incluirFichaMedica(fichaMedicaRequest);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Paciente já possui ficha médica cadastrada!", response.getBody());
    }

    @Test
    public void testBuscarFichaMedicaPorNomeSuccess() {
        // Arrange
        String nomePaciente = "Nome do Paciente";
        FichaMedica fichaMedica = new FichaMedica(/* valores de teste */);
        when(fichaMedicaServiceMock.buscarFichaMedicaPorNomePaciente(nomePaciente)).thenReturn(fichaMedica);

        // Act
        ResponseEntity<FichaMedica> response = fichaMedicaController.buscarFichaMedicaPorNome(nomePaciente);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fichaMedica, response.getBody());
    }

    @Test
    public void testExcluirFichaMedicaPorNomeSuccess() {
        // Arrange
        String nomePaciente = "Nome do Paciente";
        when(fichaMedicaServiceMock.excluirFichaMedicaPorNomePaciente(nomePaciente)).thenReturn(true);

        // Act
        ResponseEntity<String> response = fichaMedicaController.excluirFichaMedicaPorNome(nomePaciente);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ficha médica excluída com sucesso", response.getBody());
    }

    @Test
    public void testAtualizarFichaMedicaPorNomeSuccess() {
        // Arrange
        String nomePaciente = "Nome do Paciente";
        FichaMedicaRequest fichaMedicaRequest = new FichaMedicaRequest(/* valores de teste */);
        FichaMedica fichaMedicaAtualizada = new FichaMedica(/* valores de teste */);
        when(fichaMedicaServiceMock.atualizarFichaMedicaPorNomePaciente(nomePaciente, fichaMedicaRequest)).thenReturn(fichaMedicaAtualizada);

        // Act
        ResponseEntity<FichaMedica> response = fichaMedicaController.atualizarFichaMedicaPorNome(nomePaciente, fichaMedicaRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fichaMedicaAtualizada, response.getBody());
    }
}

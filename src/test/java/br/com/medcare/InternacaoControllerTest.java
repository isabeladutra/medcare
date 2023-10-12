package br.com.medcare;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.medcare.services.InternacaoService;
import br.com.medcare.services.UserRepositoryService;
import br.com.medcare.model.Internacao;
import br.com.medcare.model.InternacaoRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class InternacaoControllerTest {

    private InternacaoService internacaoServiceMock;
    private UserRepositoryService userRepositoryServiceMock;
    private InternacaoController internacaoController;

    @BeforeEach
    public void setUp() {
        internacaoServiceMock = mock(InternacaoService.class);
        userRepositoryServiceMock = mock(UserRepositoryService.class);
        internacaoController = new InternacaoController();
        internacaoController.setInternacaoService(internacaoServiceMock);
        internacaoController.setUserse(userRepositoryServiceMock);
    }

    @Test
    public void testAdicionarInternacaoSuccess() {
        // Arrange
        InternacaoRequest internacaoRequest = new InternacaoRequest(/* valores de teste */);
        when(internacaoServiceMock.buscarInternacoesPorNomePaciente(anyString())).thenReturn(new ArrayList<>());
        when(internacaoServiceMock.adicionarInternacao(any(InternacaoRequest.class))).thenReturn(new Internacao());
        
        // Simulando autenticação do médico
        Authentication authentication = new UsernamePasswordAuthenticationToken("medico@teste.com", "senha");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        ResponseEntity<String> response = internacaoController.adicionarInternacao(internacaoRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Internacao registrada com sucesso", response.getBody());
    }

    @Test
    public void testAdicionarInternacaoConflict() {
        // Arrange
    	LocalDateTime data = LocalDateTime.now();
        InternacaoRequest internacaoRequest = new InternacaoRequest(/* valores de teste */);
        internacaoRequest.setPacienteNome("sss");
        internacaoRequest.setDataEntrada(data);
        Internacao inter = new Internacao();
        inter.setDataEntradaInternacao(data);
        List<Internacao> lista = new ArrayList<>();
        lista.add(inter);
        when(internacaoServiceMock.buscarInternacoesPorNomePaciente(anyString())).thenReturn(lista);
        
        // Simulando autenticação do médico
        Authentication authentication = new UsernamePasswordAuthenticationToken("medico@teste.com", "senha");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        ResponseEntity<String> response = internacaoController.adicionarInternacao(internacaoRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Já existe uma internação registrada para esse paciente nessa data", response.getBody());
    }

    @Test
    public void testBuscarInternacoesPorNomePacienteSuccess() {
        // Arrange
        String nomePaciente = "Nome do Paciente";
        List<Internacao> internacoes = Arrays.asList(new Internacao(/* valores de teste */));
        when(internacaoServiceMock.buscarInternacoesPorNomePaciente(nomePaciente)).thenReturn(internacoes);

        // Act
        ResponseEntity<List<Internacao>> response = internacaoController.buscarInternacoesPorNomePaciente(nomePaciente);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(internacoes, response.getBody());
    }

    @Test
    public void testAtualizarInternacaoSuccess() {
        // Arrange
        String nomePaciente = "Nome do Paciente";
        String dataEntrada = "2023-10-11 10:30:00";
        InternacaoRequest internacaoRequest = new InternacaoRequest(/* valores de teste */);
        Internacao internacaoAtualizada = new Internacao(/* valores de teste */);
        List<Internacao> lista = new ArrayList<>();
        when(internacaoServiceMock.buscarInternacoesPorNomePaciente(anyString())).thenReturn(lista);
        when(internacaoServiceMock.atualizarInternacao(anyString(), any(LocalDateTime.class), any(InternacaoRequest.class))).thenReturn(internacaoAtualizada);
        
        // Simulando autenticação do médico
        Authentication authentication = new UsernamePasswordAuthenticationToken("medico@teste.com", "senha");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        ResponseEntity<?> response = internacaoController.atualizarInternacao(nomePaciente, dataEntrada, internacaoRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(internacaoAtualizada, response.getBody());
    }

    @Test
    public void testExcluirInternacaoSuccess() {
        // Arrange
        String nomePaciente = "Nome do Paciente";
        String dataEntrada = "2023-10-11 10:30:00";
  
        when(internacaoServiceMock.excluirInternacao(anyString(), any(LocalDateTime.class))).thenReturn(true);
        
        // Simulando autenticação do médico
        Authentication authentication = new UsernamePasswordAuthenticationToken("medico@teste.com", "senha");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        ResponseEntity<String> response = internacaoController.excluirInternacao(nomePaciente, dataEntrada);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registro de internação excluído com sucesso", response.getBody());
    }
}


package br.com.medcare;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigInteger;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.medcare.services.MedicoService;
import br.com.medcare.services.UserRepositoryService;
import br.com.medcare.exceptions.MedicoNaoEncontradoException;
import br.com.medcare.model.Medico;
import br.com.medcare.model.MedicoRequest;
import br.com.medcare.model.User;

public class MedicoControllerTests {

    private MedicoService medicoServiceMock;
    private UserRepositoryService userServiceMock;
    private MedicoController medicoController;

    @BeforeEach
    public void setUp() {
        medicoServiceMock = mock(MedicoService.class);
        userServiceMock = mock(UserRepositoryService.class);
        medicoController = new MedicoController();
        medicoController.setMedicoService(medicoServiceMock);
        medicoController.setUserService(userServiceMock);
    }

    @Test
    public void testCadastraMedicoSuccess() {
        // Arrange
        MedicoRequest medicoRequest = new MedicoRequest(/* medicoRequest com valores de teste */);
        when(userServiceMock.buscaMedicoouPaciente(anyString())).thenReturn(false);
        when(medicoServiceMock.buscarMedicoPorNome(anyString())).thenReturn(null);
        when(medicoServiceMock.salvarMedico(any())).thenReturn(new Medico());

        // Act
        ResponseEntity<String> response = medicoController.cadastraMedico(medicoRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Médico criado com sucesso", response.getBody());
    }

    @Test
    public void testCadastraMedicoConflict() {
        // Arrange
        MedicoRequest medicoRequest = new MedicoRequest(/* medicoRequest com valores de teste */);
        medicoRequest.setEmail("a@gmail.com");
        when(userServiceMock.buscaMedicoouPaciente(anyString())).thenReturn(true);
        when(medicoServiceMock.buscarMedicoPorNome(anyString())).thenReturn(new Medico());
    
        // Act
        ResponseEntity<String> response = medicoController.cadastraMedico(medicoRequest);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Já existe um medico cadastrado com esse nome e email", response.getBody());
    }

    @Test
    public void testAtualizarMedicoSuccess() {
        // Arrange
        MedicoRequest medicoRequest = new MedicoRequest();
        medicoRequest.setNome("Alan");
        medicoRequest.setEmail("a@gmail.com");
        medicoRequest.setPassword("123");
        Medico med = new Medico();
        med.setNome("Alan");
        PasswordEncoder PASS = mock(PasswordEncoder.class);
        
        medicoController.setPasswordEncoder(PASS);
        User user = new User();
        user.setEmail("a@gmail.com");
        user.setPassword("123");
        med.setUser(user);
        when(medicoServiceMock.buscarMedicoPorNome("Alan")).thenReturn(med);

        // Act
        ResponseEntity<String> response = medicoController.atualizarMedico(medicoRequest, "Alan");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Médico atualizado com sucesso", response.getBody());
    }

    @Test
    public void testAtualizarMedicoNotFound() {
        // Arrange
        MedicoRequest medicoRequest = new MedicoRequest(/* medicoRequest com valores de teste */);
        when(medicoServiceMock.buscarMedicoPorNome(anyString())).thenReturn(null);

        // Act
        ResponseEntity<String> response = medicoController.atualizarMedico(medicoRequest, "NomeNaoExistente");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Médico não encontrado", response.getBody());
    }

    @Test
    public void testExcluirMedicoSuccess() throws MedicoNaoEncontradoException {
        // Arrange
        BigInteger crm = BigInteger.valueOf(12345);

        // Act
        ResponseEntity<String> response = medicoController.excluirMedico(crm);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Medico excluído com sucesso", response.getBody());
    }

    @Test
    public void testExcluirMedicoNotFound() throws MedicoNaoEncontradoException {
        // Arrange
        BigInteger crm = BigInteger.valueOf(12345);
        doThrow(new MedicoNaoEncontradoException("medico nao encontrado")).when(medicoServiceMock).excluirMedico(crm);

        // Act
        ResponseEntity<String> response = medicoController.excluirMedico(crm);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Medico não encontrado", response.getBody());
    }
}

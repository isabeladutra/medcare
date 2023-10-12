package br.com.medcare;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import br.com.medcare.model.Paciente;
import br.com.medcare.model.PacienteRequest;
import br.com.medcare.model.User;
import br.com.medcare.dto.PacienteDTO;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.services.ConsultaService;
import br.com.medcare.services.PacienteService;
import br.com.medcare.services.UserRepositoryService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class PacienteControllerTest {

   
    private PacienteController pacienteController;

   
    private PacienteService pacienteService;

    
    private ConsultaService consultaService;
    
    private UserRepositoryService userService;
    
    @BeforeEach
    public void setUp() {
    	pacienteController = new PacienteController();
    	pacienteService = mock(PacienteService.class);
    	consultaService = mock(ConsultaService.class);
    	userService = mock(UserRepositoryService.class);
    	pacienteController.setUserService(userService);
    	pacienteController.setConsultaService(consultaService);
    	pacienteController.setPacienteService(pacienteService);
    }

    @Test
    void testListarConsultasDoPacientePacienteNaoEncontrado() throws PacienteNaoEncontradoException {
    	 Mockito.doThrow(new PacienteNaoEncontradoException("Paciente não encontrado"))
         .when(pacienteService).buscarPorCPF(anyString());

        ResponseEntity<?> response = pacienteController.listarConsultasDoPaciente("123456789");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Paciente não está cadastrado no sistema", response.getBody());
    }

    @Test
    void testListarConsultasDoPacienteSucesso() throws PacienteNaoEncontradoException {
        Paciente paciente = new Paciente();
      
        when(pacienteService.buscarPorCPF(any(String.class))).thenReturn(paciente);

        ResponseEntity<?> response = pacienteController.listarConsultasDoPaciente("123456789");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ArrayList<>(), response.getBody());
    }

    @Test
    void testExcluirPacientePacienteNaoEncontrado() throws PacienteNaoEncontradoException {
        PacienteService pacienteService = Mockito.mock(PacienteService.class);
        PacienteController pacienteController = new PacienteController();
        pacienteController.setPacienteService(pacienteService);

        // Configurar o comportamento do método void usando doThrow()
        doThrow(PacienteNaoEncontradoException.class).when(pacienteService).excluirPaciente(any(String.class));

        ResponseEntity<String> response = pacienteController.excluirPaciente("123456789");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Paciente não encontrado", response.getBody());
    }

    @Test
    void testExcluirPacienteSucesso() {
        ResponseEntity<String> response = pacienteController.excluirPaciente("123456789");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Paciente excluído com sucesso", response.getBody());
    }

    @Test
    void testCadastrarPacientePacienteJaExistente() throws ParseException {
        PacienteRequest pacienteRequest = new PacienteRequest(/* valores de teste */);
        pacienteRequest.setEmail("a@gmail.com");
        when(pacienteService.buscarPacientePorNome(any(String.class))).thenReturn(new Paciente());
        when(userService.buscaMedicoouPaciente(anyString())).thenReturn(true);

        ResponseEntity<String> response = pacienteController.cadastraPaciente(pacienteRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Já existe um paciente cadastrado com esse nome e email", response.getBody());
    }

    @Test
    void testCadastrarPacienteSucesso() throws ParseException {
        PacienteRequest pacienteRequest = new PacienteRequest(/* valores de teste */);
        pacienteRequest.setEmail("A@gmail.com");
        pacienteRequest.setPassword("123");
        pacienteRequest.setNome("aaa");
        pacienteRequest.setDataDeNascimento("28/02/2000");
        when(pacienteService.buscarPacientePorNome(any(String.class))).thenReturn(null);
        when(userService.salvaPaciente(any(String.class), any(String.class), any(String.class))).thenReturn(new User());
        when(pacienteService.salvarPaciente(any(Paciente.class))).thenReturn(new Paciente());

        ResponseEntity<String> response = pacienteController.cadastraPaciente(pacienteRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Paciente criado com sucesso", response.getBody());
    }

    @Test
    void testAtualizarPacientePacienteNaoEncontrado() {
        PacienteRequest pacienteRequest = new PacienteRequest(/* valores de teste */);
        when(pacienteService.buscarPacientePorNome(any(String.class))).thenReturn(null);

        ResponseEntity<String> response = pacienteController.atualizarPaciente(pacienteRequest, "NomeAntigo");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Paciente não encontrado", response.getBody());
    }

    @Test
    void testAtualizarPacienteSucesso() throws ParseException {
        PacienteRequest pacienteRequest = new PacienteRequest(/* valores de teste */);
        Paciente pacienteExistente = new Paciente();
        pacienteExistente.setNome("bbb");
        pacienteRequest.setNome("aa");
        pacienteRequest.setEmail("a@gmail.com");
        User user = new User();
        user.setEmail("a@gmail.com");
        pacienteExistente.setUser(user);
        pacienteRequest.setDataDeNascimento("28/02/2000");
        when(pacienteService.buscarPacientePorNome("NomeAntigo")).thenReturn(pacienteExistente);

        ResponseEntity<String> response = pacienteController.atualizarPaciente(pacienteRequest, "NomeAntigo");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Paciente atualizado com sucesso", response.getBody());
    }

    @Test
    void testListarPacientesSucesso() {
        List<Paciente> pacientes = new ArrayList<>();
 
        when(pacienteService.listarTodosPacientes()).thenReturn(pacientes);

        ResponseEntity<List<PacienteDTO>> response = pacienteController.listarPacientes();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
      
    }
}


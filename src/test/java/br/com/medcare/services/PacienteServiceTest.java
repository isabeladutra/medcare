package br.com.medcare.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.model.Medicamentos;
import br.com.medcare.model.Paciente;
import br.com.medcare.model.User;
import br.com.medcare.services.PacienteRepository;
import br.com.medcare.services.PacienteService;
import br.com.medcare.services.UserRepository;

class PacienteServiceTest {

   
    private PacienteRepository pacienteRepository;

    
    private UserRepository userRepository;

    
    private PacienteService pacienteService;
    
    private ConsultaRepository consuRepo;
    
    private MedicamentosRepository medRepo;
    
    private InternacaoRepository interRepo;
    
    private FichaMedicaRepository fichaRepo;

    @BeforeEach
    void setUp() {
    	 pacienteService = new PacienteService();
    	userRepository = mock(UserRepository.class);
    	medRepo = mock(MedicamentosRepository.class);
    	pacienteService.setMedRepo(medRepo);
    	interRepo = mock(InternacaoRepository.class);
    	fichaRepo = mock(FichaMedicaRepository.class);
    	pacienteService.setFichaRepo(fichaRepo);
    	pacienteService.setInternacaoRepo(interRepo);
    	consuRepo = mock(ConsultaRepository.class);
    	pacienteRepository = mock(PacienteRepository.class);
    	pacienteService.setRepo(pacienteRepository);
    	pacienteService.setUserRepo(userRepository);
    	pacienteService.setConsultaRepo(consuRepo);
    	pacienteService.setRepo(pacienteRepository);
    }

    @Test
    void testBuscarPorCPF() throws PacienteNaoEncontradoException {
        // Arrange
        String cpf = "123456789";
        Paciente paciente = new Paciente();
        paciente.setCpf(cpf);
        when(pacienteRepository.findByCpf(cpf)).thenReturn(Optional.of(paciente));

        // Act
        Paciente result = pacienteService.buscarPorCPF(cpf);

        // Assert
        assertNotNull(result);
        assertEquals(cpf, result.getCpf());
    }

    @Test
    void testBuscarPorCPFPacienteNaoEncontrado() {
        // Arrange
        String cpf = "123456789";
        when(pacienteRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(PacienteNaoEncontradoException.class, () -> {
            pacienteService.buscarPorCPF(cpf);
        });
    }

    @Test
    void testExcluirPaciente() throws PacienteNaoEncontradoException {
        // Arrange
        String cpf = "123456789";
        Paciente paciente = new Paciente();
        paciente.setCpf(cpf);
        
        User user = new User();
        user.setEmail("test@example.com");
        paciente.setUser(user);
        Optional<User> opt = Optional.ofNullable(user);
        List<Medicamentos> listaDeMedicamentos = new ArrayList<>();
        when(pacienteRepository.findByCpf(cpf)).thenReturn(Optional.of(paciente));
        when (medRepo.findByPaciente(any())).thenReturn(listaDeMedicamentos);
        when(userRepository.findByEmail(anyString())).thenReturn(opt);
        
        
        // Act
        pacienteService.excluirPaciente(cpf);

        // Assert
        verify(pacienteRepository, times(1)).delete(paciente);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testExcluirPacientePacienteNaoEncontrado() {
        // Arrange
        String cpf = "123456789";
        when(pacienteRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(PacienteNaoEncontradoException.class, () -> {
            pacienteService.excluirPaciente(cpf);
        });
    }

    @Test
    void testListarTodosPacientes() {
        // Arrange
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(new Paciente());
        when(pacienteRepository.findAll()).thenReturn(pacientes);

        // Act
        List<Paciente> result = pacienteService.listarTodosPacientes();

        // Assert
        assertNotNull(result);
        assertEquals(pacientes.size(), result.size());
    }
}

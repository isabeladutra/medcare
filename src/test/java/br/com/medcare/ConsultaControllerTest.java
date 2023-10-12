package br.com.medcare;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.time.LocalDateTime;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import br.com.medcare.dto.ReagendamentoConsultaDTO;
import br.com.medcare.exceptions.ConsultaNaoEncontradaException;
import br.com.medcare.exceptions.MedicoNaoEncontradoException;
import br.com.medcare.exceptions.PacienteNaoEncontradoException;
import br.com.medcare.services.ConsultaService;
import br.com.medcare.services.MedicoService;
import br.com.medcare.services.PacienteService;
import br.com.medcare.model.Consulta;
import br.com.medcare.model.ConsultaRequest;
import br.com.medcare.model.Medico;
import br.com.medcare.model.Paciente;

public class ConsultaControllerTest {

    private ConsultaService consultaServiceMock;
    private MedicoService medicoServiceMock;
    private PacienteService pacienteServiceMock;
    private ConsultaController consultaController;

    @BeforeEach
    public void setUp() {
        consultaServiceMock = mock(ConsultaService.class);
        medicoServiceMock = mock(MedicoService.class);
        pacienteServiceMock = mock(PacienteService.class);
        consultaController = new ConsultaController();
        consultaController.setConsultaService(consultaServiceMock);
        consultaController.setMedicoService(medicoServiceMock);
        consultaController.setPacienteService(pacienteServiceMock);
    }

    @Test
    public void testAgendarConsultaSuccess() throws MedicoNaoEncontradoException, PacienteNaoEncontradoException {
        // Arrange
        ConsultaRequest consultaRequest = new ConsultaRequest(/* valores de teste */);
        Consulta consulta = new Consulta(/* valores de teste */);
        consultaRequest.setCrmMedico(BigInteger.valueOf(12345));
        consultaRequest.setCpfpaciente("1234");
        consulta.setDataHora(LocalDateTime.now());
        when(consultaServiceMock.agendarConsulta(any(Consulta.class))).thenReturn(consulta);
        Medico med = new Medico();
        med.setNome("aaa");
        Paciente pac = new Paciente();
        pac.setNome("bbb");
        consulta.setMedico(med);
        consulta.setPaciente(pac);
        when(medicoServiceMock.buscarPorCRM(any(BigInteger.class))).thenReturn(med);
        when(pacienteServiceMock.buscarPorCPF(any(String.class))).thenReturn(pac);

        // Act
        ResponseEntity<String> response = consultaController.agendarConsulta(consultaRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        // Adicione aqui mais verificações conforme necessário para o corpo da resposta
    }

    @Test
    public void testReagendarConsultaSuccess() throws ConsultaNaoEncontradaException, MedicoNaoEncontradoException, PacienteNaoEncontradoException {
        // Arrange
        ReagendamentoConsultaDTO reagendamentoDTO = new ReagendamentoConsultaDTO(/* valores de teste */);
        when(consultaServiceMock.reagendarConsulta(any(ReagendamentoConsultaDTO.class), any(String.class))).thenReturn(true);
        
        
     // Configuração da autenticação do paciente
        Authentication authentication = new UsernamePasswordAuthenticationToken("paciente@teste.com", "senha");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        ResponseEntity<String> response = consultaController.reagendarConsulta(reagendamentoDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Consulta reagendada com sucesso", response.getBody());
    }

    @Test
    public void testCancelarConsultaSuccess() {
        // Arrange
    	 // Configuração da autenticação do paciente
        Authentication authentication = new UsernamePasswordAuthenticationToken("paciente@teste.com", "senha");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LocalDateTime data = LocalDateTime.now();
        when(consultaServiceMock.cancelarConsulta(any(LocalDateTime.class), any(String.class))).thenReturn(true);

        // Act
        ResponseEntity<String> response = consultaController.cancelarConsulta(data);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Consulta cancelada com sucesso", response.getBody());
    }
}

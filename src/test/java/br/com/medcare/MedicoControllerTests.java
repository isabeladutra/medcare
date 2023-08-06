package br.com.medcare;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.medcare.model.Medico;
import br.com.medcare.model.User;
import br.com.medcare.model.MedicoRequest;
import br.com.medcare.services.MedicoService;
import br.com.medcare.services.UserRepositoryService;

public class MedicoControllerTests {

	   MedicoService medicoService = mock(MedicoService.class);
       UserRepositoryService userRepositoryService = mock(UserRepositoryService.class);
       private AutoCloseable closeable;
       
   	@BeforeEach
   	public void initMocks() {
   		 closeable = MockitoAnnotations.openMocks(this);
   	}
   	
   	@AfterEach 
   	public void releaseMocks() throws Exception {
           closeable.close();
       }
	
	
    @Test
    public void testCadastrarMedico() {
     

        MedicoController controller = new MedicoController();
        controller.medicoService = medicoService;
        controller.userService = userRepositoryService;

        // Defina o comportamento esperado para os mocks
        when(userRepositoryService.salvaMedico(any(), any())).thenReturn(new User());
        when(medicoService.buscarMedicoPorNome(any())).thenReturn(null);
        when(medicoService.salvarMedico(any())).thenReturn(new Medico());

        // Chame o método do controlador
        MedicoRequest medicoRequest = new MedicoRequest();
        ResponseEntity<String> response = controller.cadastraMedico(medicoRequest);

        // Verifique o resultado
        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getBody().equals("Médico criado com sucesso");

        // Verifique se os métodos dos serviços foram chamados com os argumentos corretos
        verify(userRepositoryService).salvaMedico(any(), any());
        verify(medicoService).salvarMedico(any());
    }
    
    
    @Test
    public void testAtualizarMedico() {
        MedicoController controller = new MedicoController();
        controller.medicoService = medicoService;

        // Crie um mock de MedicoRequest
        MedicoRequest medicoRequest = new MedicoRequest();
        medicoRequest.setNome("Nome do Médico"); // Altere para o nome do médico que você deseja testar

        // Defina o comportamento esperado para o mock de MedicoService
        Medico medicoExistente = new Medico();
        medicoExistente.setUser(new User()); // Suponha que o médico existente possui um usuário associado
        when(medicoService.buscarMedicoPorNome(medicoRequest.getNome())).thenReturn(medicoExistente);
        when(medicoService.salvarMedico(any())).thenReturn(medicoExistente);

        // Chame o método do controlador
        ResponseEntity<String> response = controller.atualizarMedico(medicoRequest);

        // Verifique o resultado
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().equals("Médico atualizado com sucesso");

        // Verifique se o método do MedicoService foi chamado com o médico existente
        verify(medicoService).salvarMedico(medicoExistente);
    }
}

package br.com.medcare.mappers;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import br.com.medcare.dto.PacienteDTO;
import br.com.medcare.model.Paciente;
import br.com.medcare.model.User;

public class PacienteDTOMapperTest {

    @Test
    public void testMapper() {
        // Crie uma instância de Paciente para testar
        User user = new User();
        user.setEmail("teste@example.com");

        Paciente paciente = new Paciente();
        paciente.setCpf("123456789");
        paciente.setEndereco("Endereço do Paciente");
        paciente.setTelefone("1234567890");
        paciente.setNome("Nome do Paciente");
        paciente.setDataDenascimento(new Date());
        paciente.setUser(user);

        // Chame o método mapper
        PacienteDTO pacienteDTO = PacienteDTOMapper.mapper(paciente);

        // Verifique se os campos mapeados estão corretos
        assertEquals("123456789", pacienteDTO.getCpf());
        assertEquals("teste@example.com", pacienteDTO.getEmail());
        assertEquals("Endereço do Paciente", pacienteDTO.getEnderecoCompleto());
        assertEquals("1234567890", pacienteDTO.getTelefone());
        assertEquals("Nome do Paciente", pacienteDTO.getNome());
       
    }
}


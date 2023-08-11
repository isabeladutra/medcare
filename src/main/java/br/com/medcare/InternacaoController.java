package br.com.medcare;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.medcare.model.Internacao;
import br.com.medcare.model.InternacaoRequest;
import br.com.medcare.services.InternacaoService;
import jakarta.annotation.security.RolesAllowed;



@RestController
@RequestMapping("/internacao")
public class InternacaoController {
	
	@Autowired
	private InternacaoService internacaoService;
	

	@PostMapping("/adicionar")
	@RolesAllowed("ROLE_MEDICO")
    public ResponseEntity<Internacao> adicionarInternacao(@RequestBody InternacaoRequest internacaoRequest) {
        Internacao novaInternacao = internacaoService.adicionarInternacao(internacaoRequest);

        if (novaInternacao != null) {
            return new ResponseEntity<>(novaInternacao, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
	
    @GetMapping("/paciente/{nomePaciente}")
    @RolesAllowed("ROLE_MEDICO")
    public ResponseEntity<List<Internacao>> buscarInternacoesPorNomePaciente(@PathVariable String nomePaciente) {
        List<Internacao> internacoes = internacaoService.buscarInternacoesPorNomePaciente(nomePaciente);

        if (internacoes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(internacoes, HttpStatus.OK);
        }
    }
    
    @PutMapping("/atualizar")
    @RolesAllowed("ROLE_MEDICO")
    public ResponseEntity<Internacao> atualizarInternacao(
            @RequestParam String nomePaciente,
            @RequestParam String dataEntrada,
            @RequestBody InternacaoRequest internacaoRequest
    ) {
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dataEntradaDateTime = LocalDateTime.parse(dataEntrada, formatter);
        Internacao internacaoAtualizada = internacaoService.atualizarInternacao(nomePaciente, dataEntradaDateTime, internacaoRequest);

        if (internacaoAtualizada != null) {
            return new ResponseEntity<>(internacaoAtualizada, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
    @DeleteMapping("/excluir")
    @RolesAllowed("ROLE_MEDICO")
    public ResponseEntity<String> excluirInternacao(
            @RequestParam String nomePaciente,
            @RequestParam String dataEntrada
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dataEntradaDateTime = LocalDateTime.parse(dataEntrada, formatter);

        boolean internacaoExcluida = internacaoService.excluirInternacao(nomePaciente, dataEntradaDateTime);

        if (internacaoExcluida) {
            return new ResponseEntity<>("Registro de internação excluído com sucesso", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Registro de internação não encontrado", HttpStatus.NOT_FOUND);
        }
    }
}

package br.com.medcare.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.medcare.model.Role;
import jakarta.annotation.PostConstruct;

@Service
public class RoleRepositoryService {
	
	@Autowired
	RoleRepository roleRepo;
	
	/* @PostConstruct
	    public void init() {
	        Salva();
	    }
	
	public void Salva() {
		Role medico = new Role("ROLE_MEDICO");
        Role paciente = new Role("ROLE_PACIENTE");
        
         
        roleRepo.saveAll(List.of(medico, paciente));
         
        long count = roleRepo.count();
      
	}*/

}

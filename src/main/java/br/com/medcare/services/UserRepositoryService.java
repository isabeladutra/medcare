package br.com.medcare.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.medcare.model.*;
import java.util.HashSet;
import java.util.Optional;
 
@Service
public class UserRepositoryService  {
	
	
	@Autowired private UserRepository repo;
	
	@Autowired private RoleRepository roleRepo;
	//A função da classe JwtUserDetailsService é buscar os detalhes do usuário no sistema de armazenamento de dados, como um banco de dados ou outro serviço externo, com base no nome de usuário fornecido. 
	public User salvaMedico(String email, String senha, String nome) {
		 BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String password = passwordEncoder.encode(senha);
	       
	        Set<Role> roles = new HashSet<>();
	        Role role = roleRepo.getById(1);
	        
	        roles.add(role);
	        User newUser = new User();
	        newUser.setEmail(email);
	        newUser.setPassword(password);
	        newUser.setRoles(roles);
	        newUser.setNome(nome);
	        User savedUser = repo.save(newUser);
	        return savedUser;
	         
	        
	}
	
	public User salvaPaciente(String email, String senha, String nome) {
		 BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String password = passwordEncoder.encode(senha);
	       
	        Set<Role> roles = new HashSet<>();
	        Role role = roleRepo.getById(2);
	        
	        roles.add(role);
	        User newUser = new User();
	        newUser.setEmail(email);
	        newUser.setPassword(password);
	        newUser.setRoles(roles);
	        newUser.setNome(nome);
	        
	        User savedUser = repo.save(newUser);
	        return savedUser;
	         
	        
	}
	
	public boolean buscaMedicoouPaciente(String email) {
		Optional<User> retorno =repo.findByEmail(email);
		
		  if (retorno.isPresent()) {
		        // O Optional retornou um valor
		        return true;
		    } else {
		        // O Optional não retornou um valor
		        return false;
		    }
	}

	public String buscaNomeUser(String email) {
		Optional<User> user = repo.findByEmail(email);
		
		if(user.isPresent()) {
			return user.get().getNome();
		}
		return null;
	}
	
}
package br.com.medcare.services;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medcare.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}

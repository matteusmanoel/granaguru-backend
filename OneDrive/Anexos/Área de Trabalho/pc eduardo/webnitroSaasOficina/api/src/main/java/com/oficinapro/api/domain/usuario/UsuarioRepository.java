package com.oficinapro.api.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Esse método serve pro Spring Security achar o cara pelo login
    UserDetails findByLogin(String login);
}
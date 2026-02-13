package com.oficinapro.api.infra.security;

import com.oficinapro.api.domain.empresa.Empresa;
import com.oficinapro.api.domain.usuario.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public Empresa getEmpresaLogada() {
        // Pega o usuário que o SecurityFilter colocou no "contexto"
        var usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return usuario.getEmpresa();
    }
}
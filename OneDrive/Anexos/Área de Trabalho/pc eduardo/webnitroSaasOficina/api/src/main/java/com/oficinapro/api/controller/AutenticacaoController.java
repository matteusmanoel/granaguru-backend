package com.oficinapro.api.controller;

// 👇 IMPORTANTE: Os Records (DTOs) ficam junto com o domínio do Usuário

import com.oficinapro.api.domain.empresa.EmpresaRepository;
import com.oficinapro.api.domain.usuario.DadosAutenticacao;
import com.oficinapro.api.domain.usuario.DadosCadastroUsuario;
import com.oficinapro.api.domain.usuario.Usuario;
import com.oficinapro.api.domain.usuario.UsuarioRepository;
import com.oficinapro.api.infra.security.DadosTokenJWT;
import com.oficinapro.api.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());

        // O Spring autentica
        var authentication = manager.authenticate(authenticationToken);

        // Gera o Token
        // 👇 O cast (Usuario) só funciona se tiver o import correto lá em cima
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

    @PostMapping("/register")
    @org.springframework.transaction.annotation.Transactional // Bom pra garantir que salva tudo ou nada
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroUsuario dados) {
        // Verifica se já existe
        if (repository.findByLogin(dados.login()) != null) return ResponseEntity.badRequest().build();

        // Criptografa
        String senhaCriptografada = passwordEncoder.encode(dados.senha());

        // Busca a empresa
        var empresa = empresaRepository.findById(dados.empresaId()).orElseThrow();

        // Cria o usuário vinculado à empresa
        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(dados.login());
        novoUsuario.setSenha(senhaCriptografada);
        novoUsuario.setEmpresa(empresa);
        novoUsuario.setRole("ADMIN");

        repository.save(novoUsuario);

        return ResponseEntity.ok().build();
    }
}
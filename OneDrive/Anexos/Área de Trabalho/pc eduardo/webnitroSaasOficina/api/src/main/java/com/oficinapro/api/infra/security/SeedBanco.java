package com.oficinapro.api.infra;

import com.oficinapro.api.domain.empresa.Empresa;
import com.oficinapro.api.domain.empresa.EmpresaRepository;
import com.oficinapro.api.domain.usuario.Usuario;
import com.oficinapro.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class SeedBanco implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("🌱 INICIANDO SEED DO BANCO DE DADOS...");

        // 1. Garante que a Empresa existe (Se não tiver, cria)
        Empresa empresa;
        if (empresaRepository.count() == 0) {
            empresa = new Empresa();
            // empresa.setId(1L); // O banco gera automático geralmente, mas se precisar forçar, descomente
            empresa.setNomeFantasia("Oficina do Edu");
            empresa.setCnpj("00.000.000/0001-91");
            empresa.setTelefone("45999999999");
            empresa = empresaRepository.save(empresa);
            System.out.println("🏢 Empresa criada!");
        } else {
            empresa = empresaRepository.findAll().get(0); // Pega a primeira que achar
        }

        // 2. Verifica se o ADMIN já existe. Se existir, deleta pra recriar (reset de senha)
        var adminExistente = usuarioRepository.findByLogin("admin");
        if (adminExistente != null) {
            // Precisamos fazer um cast ou usar o ID pra deletar
            usuarioRepository.deleteById(((Usuario) adminExistente).getId());

            usuarioRepository.flush();

            System.out.println("🧹 Admin antigo apagado (limpeza de senha)...");
        }

        // 3. Cria o Admin novinho em folha
        Usuario admin = new Usuario();
        admin.setLogin("admin");
        admin.setSenha(passwordEncoder.encode("123456")); // 🔐 AQUI ESTÁ O SEGREDO!
        admin.setRole("ADMIN");
        admin.setEmpresa(empresa);

        usuarioRepository.save(admin);

        System.out.println("✅ USUÁRIO ADMIN PRONTO!");
        System.out.println("🔑 Login: admin");
        System.out.println("🔑 Senha: 123456");
        System.out.println("🌱 SEED FINALIZADO COM SUCESSO!");
    }
}
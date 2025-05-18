//AuthenticationService.java
package app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import app.config.JwtServiceGenerator;
import app.entities.Usuario;
import app.repositories.UsuarioRepository;

@Service
public class LoginService {

	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private JwtServiceGenerator jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;


	
	public String logar(Login login) {

		String token = this.gerarToken(login);
		return token;

	}



	public String gerarToken(Login login) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						login.getUsername(),
						login.getPassword()
						)
				);
		Usuario user = repository.findByEmail(login.getUsername()).get();
		String jwtToken = jwtService.generateToken(user);
		return jwtToken;
	}
}

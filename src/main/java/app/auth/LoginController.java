package app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = {
    "http://granaguru.local",
    "https://granaguru.local",
    "http://www.granaguru.local",
    "https://www.granaguru.local",
    "http://192.168.56.10",
    "http://frontend",
    "http://api.granaguru.local:8080"
}, 
allowCredentials = "true",
allowedHeaders = "*",
exposedHeaders = {"Authorization"},
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, 
           RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD})
public class LoginController {

	@Autowired
	private LoginService loginService;

	@PostMapping
	public ResponseEntity<String> logar(@RequestBody Login login) {

		String token = loginService.logar(login);
		return new ResponseEntity<>(token, HttpStatus.OK);

	}

}

package es.urjc.code.s3_ec2_practica1.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is used to provide REST endpoints to logIn and logOut to the
 * service. These endpoints are used by Angular 2 SPA client application.
 * 
 * NOTE: This class is not intended to be modified by app developer.
 */
@RestController
public class LoginController {

	@GetMapping("/")
    public String greeting(Authentication authentication) {
        return "Spring Security In-memory Authentication Example";
    }

}
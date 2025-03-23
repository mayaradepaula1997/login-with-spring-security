package com.dev.springsecurity.controller;

/*AUTENTICAÇÃO: Vai passar o login e a semha, vamos validar se o usuarioa existe
 no BD pelo seu login e comparar as senhas,
se tudo isso for verdade, vamos montar seu token JWT,
 vamos retornar esse token para o usuario, sinalizando que esse é seu token*/

import com.dev.springsecurity.controller.dto.LoginRequest;
import com.dev.springsecurity.controller.dto.LoginResponse;
import com.dev.springsecurity.entities.User;
import com.dev.springsecurity.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;

@RestController
public class TokenController {


    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;


    public TokenController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        Optional<User> user = userRepository.findByUsername(loginRequest.username()); //buscando o usuário no BD


        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("user or password is invalid");
        }

        Instant now = Instant.now();
        var expiresIn = 300L;

        //Gerar o token JWT
        //Atributos da classe JSON chamdo de "CLAIMS"

        var claims = JwtClaimsSet.builder() //var(Long)
                .issuer("mybackend")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        //Criptogrfia do token / var(String)
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn)); //Retorna o token e tem de inspiração


    }






}

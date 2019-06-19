package com.agagrzebyk.user_jwt.security;

import com.agagrzebyk.user_jwt.model.LoginViewModel;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
//  Metoda wyzwala się gdy wysyłamy żądanie POST - na endpoint /login
//  W body żądania podajemy WAŻNE -  "username jako email" i "password"
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        //  Przechwytujemy dane z żądania uwierzytelniające
        //  i mapujemy je aby się zalogować
        LoginViewModel credentials = null;
        try{
            credentials = new ObjectMapper().readValue(request.getInputStream(), LoginViewModel.class);
        } catch (IOException e){
            e.printStackTrace();
        }

        // token który spring boot używe wewnętrznie
        // aby nas rozpoznać po przesłanych danych
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword(),
                new ArrayList<>());

        // bazując na AuthenticationManager wyzwalamy metodę authenticate
        // z naszym wewn. tokenem
        Authentication auth = authenticationManager.authenticate(authenticationToken);
        return auth;
    }


    // Jeżeli autentykacja przebiegła pomyślnie -
    // Pobieramy myUserDetails z wyniku autentykacji - z bazy danych
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        MyUserDetails userDetails = (MyUserDetails) authResult.getPrincipal();

        // Tworzy JWT Token
        String token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .sign(HMAC512(JwtProperties.SECRET.getBytes()));

        //dodaje token do response
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
    }
}

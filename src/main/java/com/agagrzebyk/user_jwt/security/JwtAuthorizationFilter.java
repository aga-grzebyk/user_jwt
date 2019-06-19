package com.agagrzebyk.user_jwt.security;

import com.agagrzebyk.user_jwt.model.User;
import com.agagrzebyk.user_jwt.repo.UserRepository;
import com.auth0.jwt.JWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    // tworzymy filtr i przesyłamy go do dalszego przetwarzania(chain)
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {
    // Pobieramy header
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)){
            chain.doFilter(request, response);
            // user nie jest zautoryzowany
            return;
        }
        // Jeżeli header jest poprawny pobieramy userDetails z bazy danych
        // i tworzymy klasę Authentication
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // filtr z autoryzacją przesyłamy do dalszych resalizacji
        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");

        if(token != null){
            String username = JWT
                    .require(HMAC512(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();
        // Przeszukujemy bazę danych czy jest user z username z token subject -
            // W naszym przypadku username to email
            // Jeżeli znaleziony pobieramy user details i tworzymy spring auth token
            // z username, password i authorities/roles
            if(username != null){
                User user = userRepository.findByUsername(username);
                MyUserDetails principal = new MyUserDetails(user);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, principal.getAuthorities());

                return auth;
            }
            return null;
        }
        return null;
    }
}

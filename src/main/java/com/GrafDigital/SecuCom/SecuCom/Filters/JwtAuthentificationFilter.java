package com.GrafDigital.SecuCom.SecuCom.Filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

// cette classe va étendre de UsernamePasswordAuthenticationFilter;
@AllArgsConstructor // pour injection authenticationManager
public class JwtAuthentificationFilter extends UsernamePasswordAuthenticationFilter {
    // cette Classe à besoin de authenticationManager pour fonctionner
    private AuthenticationManager authenticationManager;

    // Prémière méthode quand User essyae de se Loger
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // On récupère le UserName et le Password
        System.out.println("----------- attemptAuthentication ---------------");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        System.out.println(userName);
        System.out.println(password);

        // On stocke le userName et le password dans un Objet de Spring security;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    // Deuxième méthode quand l'authentification a réussi
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("------------ successfulAuthentication -----------");

        User user = (User) authResult.getPrincipal(); // permet de retourner le user authentifier;
        System.out.println("Bonjour " +user.getUsername());
        // Générons le JWT
        Algorithm algo1 = Algorithm.HMAC256("myScret2121".getBytes()); // Algorithm dencodage
        String jwtAccessToken = JWT.create()
                .withSubject(user.getUsername()) // userName
                        .withExpiresAt(new Date(System.currentTimeMillis()+2*60*1000)) // delais token 10s
                                .withIssuer(request.getRequestURL().toString()) // le nom de l'app qui a genérer le Token
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())) // convertir la liste des Rôles en string
                        .sign(algo1);

        Map<String, String> idToken = new HashMap<>();
        idToken.put("access_token", jwtAccessToken);
        idToken.put("Bonjour ",user.getUsername());
        idToken.put("Profile ",user.getAuthorities().toString());
        // Envoie le JWT au client en format JSON
        response.setContentType(APPLICATION_JSON_VALUE); // Dire qu'il sagit de format JSON
        new ObjectMapper().writeValue(response.getOutputStream(), idToken);
    }
}

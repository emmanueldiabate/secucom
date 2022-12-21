package com.GrafDigital.SecuCom.SecuCom.Filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

// Cette classe va étendre de la classe OncePerRequestFilter;
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    // on implement la méthode
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletPath().equals("/SecuCom/login")){
             filterChain.doFilter(request, response);
        }else {
            String authorizationHeader;
            authorizationHeader = request.getHeader(AUTHORIZATION);
            // vérifions si le Header n'est pas null
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                try {
                    String token = authorizationHeader.substring("Bearer ".length()); // Pour ignorer le Bearer nombre de caractère
                    Algorithm algo1 = Algorithm.HMAC256("myScret2121".getBytes()); // le même secret pour la signature
                    JWTVerifier verifier = JWT.require(algo1).build();
                    DecodedJWT decodedJWT  = verifier.verify(token); // Vérifier l'algorithm qui  créer le Token
                    String userName = decodedJWT.getSubject(); // Pour recupérer le userName
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class); // Pour recupérer les roles de claim
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>(); // Convertie les roles en liste de string
                    stream(roles).forEach(role->{
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userName, null, authorities);
                    // Maintenant on authentifie le user s'il a le droit
                    SecurityContextHolder.getContext() .setAuthentication(authenticationToken );
                    // On le laisse passer maintent
                    filterChain.doFilter(request, response );
                } catch (Exception exception ){
                    log.error("Error logging in: {}", exception.getMessage());
                    response.setHeader("error", exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    // response.sendError(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", exception.getMessage() );
                    // Envoie le JWT au client en format JSON
                    response.setContentType(APPLICATION_JSON_VALUE); // Dire qu'il sagit de format JSON
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            }
            else{
                filterChain.doFilter(request, response );
            }
        }

    }

}

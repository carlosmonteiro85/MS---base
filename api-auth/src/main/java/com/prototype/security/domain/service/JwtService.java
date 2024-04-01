package com.prototype.security.domain.service;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.prototype.security.api.dto.response.RoleRespose;
import com.prototype.security.domain.model.CredencialUsuario;
import com.prototype.security.domain.repository.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final PerfilService perfilService;

    @Autowired
    public JwtService(UserDetailsService userDetailsService, TokenRepository tokenRepository, PerfilService perfilService) {
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
        this.perfilService = perfilService;
    }

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        if (userDetails instanceof CredencialUsuario) {
            CredencialUsuario user = (CredencialUsuario) userDetails;
            extraClaims.put("role",  user.getAuthorities());
        }
        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

		public boolean validateToken(String token, String username) {
			final String tokenUsername = extractUsername(token);
			return tokenUsername.equals(username) && !isTokenExpired(token);
	}

    public boolean isTokenValid(String token, String userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public List<RoleRespose> findRoles(String token) {
        final Claims claims = extractAllClaims(token);
        List<Map<String, String>> mapRole = (List<Map<String, String>>) claims.get("role");

        List<RoleRespose> roles = new ArrayList<>();

        for (Map<String, String> roleRespose : mapRole) {
            for (String entry : roleRespose.values()) {
                if (entry.contains("ROLE_")) {
                    RoleRespose autorites = perfilService.getAutorites(entry);
                    roles.add(autorites);
                }
            }
        }
        return roles;
    }
}
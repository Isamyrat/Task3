package root.developer.security.jwt;

import io.jsonwebtoken.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import root.developer.security.model.JwtRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Slf4j
@Component
public class JwtProvider {


    @Autowired
    private Environment env;


    public String generateAccessToken(@NonNull JwtRequest jwtRequest) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        log.info("{}", env.getProperty("SECRET_KEY"));

        return Jwts.builder()
                    .setSubject("task2")
                    .setExpiration(accessExpiration)
                    .signWith(SignatureAlgorithm.HS512, env.getProperty("SECRET_KEY"))
                    .claim("roles", jwtRequest.getRoles())
                    .setIssuer(jwtRequest.getIssuer())
                    .compact();
    }


    public boolean validateAccessToken(@NonNull String token) {
        return validateToken(token, env.getProperty("SECRET_KEY"));
    }


    private boolean validateToken(@NonNull String token, @NonNull String secret) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, env.getProperty("SECRET_KEY"));
    }


    private Claims getClaims(@NonNull String token, @NonNull String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

}

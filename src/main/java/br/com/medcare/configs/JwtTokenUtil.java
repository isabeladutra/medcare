package br.com.medcare.configs;

import java.security.Key;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.medcare.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {
	
	/**
	 * Em resumo, a classe JwtTokenUtil fornece métodos para extrair informações de um token JWT, gerar novos tokens, verificar a validade de um token e manipular as reivindicações (claims) do token
	 */
	private static final long EXPIRE_DURATION = 72 * 60 * 60 * 1000; // 24 hour
	 private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
	 private static final ZoneId BRASILIA_ZONE_ID = ZoneId.of("America/Sao_Paulo");
	
	@Value("${jwt.secret}")
	private String SECRET_KEY;
	
	private static final Key SECRET_KEY2 = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	public String generateAccessToken(User user) {
		 ZonedDateTime issuedAt = ZonedDateTime.now(BRASILIA_ZONE_ID);
	        ZonedDateTime expiration = issuedAt.plusHours(72); // 72 horas de expiração
	        return Jwts.builder()
	                .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
	                .setIssuer("CodeJava")
	                .claim("roles", user.getRoles().toString())
	                .setIssuedAt(Date.from(issuedAt.toInstant()))
	                .setExpiration(Date.from(expiration.toInstant()))
	                .signWith(SECRET_KEY2)
	                .compact();
    }
	
	
	
	 public boolean validateAccessToken(String token) {
	        try {
	            Jwts.parser().setSigningKey(SECRET_KEY2).parseClaimsJws(token);
	            return true;
	        } catch (ExpiredJwtException ex) {
	            LOGGER.error("JWT expired", ex.getMessage());
	        } catch (IllegalArgumentException ex) {
	            LOGGER.error("Token is null, empty or only whitespace", ex.getMessage());
	        } catch (MalformedJwtException ex) {
	            LOGGER.error("JWT is invalid", ex);
	        } catch (UnsupportedJwtException ex) {
	            LOGGER.error("JWT is not supported", ex);
	        } catch (SignatureException ex) {
	            LOGGER.error("Signature validation failed");
	        }
	         
	        return false;
	    }
	     
	    public String getSubject(String token) {
	        return parseClaims(token).getSubject();
	    }
	     
	    public Claims parseClaims(String token) {
	        return Jwts.parser()
	                .setSigningKey(SECRET_KEY2)
	                .parseClaimsJws(token)
	                .getBody();
	    }

	
	
	
	
	
	

	/*public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	@Value("${jwt.secret}")
	private String secret;
	
	private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	//retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
    //for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		 

		    return Jwts.parserBuilder()
		            .setSigningKey(SECRET_KEY)
		            .build()
		            .parseClaimsJws(token)
		            .getBody();
	}

	//check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	//generate token for user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	//while creating the token -
	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key.
	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	//   compaction of the JWT to a URL-safe string 
	private String doGenerateToken(Map<String, Object> claims, String subject) {

	
		String encodedKey = Base64.getEncoder().encodeToString(SECRET_KEY.getEncoded());
        System.out.println(encodedKey);
	    return Jwts.builder()
	            .setClaims(claims)
	            .setSubject(subject)
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
	            .signWith(SECRET_KEY)
	            .compact();
	}

	//validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		Boolean valid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		return valid;
	}
*/
	
	
}

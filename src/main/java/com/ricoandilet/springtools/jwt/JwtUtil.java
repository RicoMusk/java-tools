package com.ricoandilet.springtools.jwt;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricoandilet.springtools.json.JsonUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
* @author rico
*
*/
public class JwtUtil {

	private static final String JWT_PAYLOAD_USER_KEY = "user";

	public static String generateTokenExpireInMinutes(Object userInfo, PrivateKey privateKey, int expire) {
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JsonUtil.stringify(userInfo))
                .setId(createJTI())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(expire))))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public static String generateTokenExpireInSeconds(Object userInfo, PrivateKey privateKey, int expire) {
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JsonUtil.stringify(userInfo))
                .setId(createJTI())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofSeconds(expire))))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    private static String createJTI() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }

    public static <T> JwtPayload<T> getInfoFromToken(String token, PublicKey publicKey, Class<T> userType) throws IOException {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        //parse generic object
        ObjectMapper mapper = JsonUtil.getObjectMapper();
        JavaType type = mapper.getTypeFactory().constructParametricType(JwtPayload.class, userType);
        JwtPayload<T> claims = mapper.readValue(body.get(JWT_PAYLOAD_USER_KEY).toString(), type);
        claims.setId(body.getId());
        claims.setExpiration(converter(body.getExpiration()));
        return claims;
    }

    public static <T> JwtPayload<T> getInfoFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        JwtPayload<T> claims = new JwtPayload<>();
        claims.setId(body.getId());
        claims.setExpiration(converter(body.getExpiration()));
        return claims;
    }

    public static LocalDateTime converter(Date date) {
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		return localDateTime;
	}
}

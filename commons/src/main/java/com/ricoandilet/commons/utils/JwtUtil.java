/*
 Copyright (C) 2018-2021 YouYu information technology (Shanghai) Co., Ltd.
 <p>
 All right reserved.
 <p>
 This software is the confidential and proprietary
 information of YouYu Company of China.
 ("Confidential Information"). You shall not disclose
 such Confidential Information and shall use it only
 in accordance with the terms of the contract agreement
 you entered into with YouYu inc.
*/
package com.ricoandilet.commons.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

/**
 * @author: rico
 * @date: 2024/1/8
 */
public class JwtUtil {

  private static final String JWT_PAYLOAD_KEY = "JWT_PAYLOAD_KEY";

  private static final String PRIVATE_KEY = "";
  private static final String PUBLIC_KEY = "";

  public static String createToken(String jwtPayLoad, int expiration) {

    try {
      PrivateKey privateKey = JwtRas.parsePrivateKey(PRIVATE_KEY);
      return Jwts.builder()
          .claim(JWT_PAYLOAD_KEY, jwtPayLoad)
          .setId(new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes())))
          .setIssuedAt(DateTime.now().toDate())
          .setExpiration(DateTime.now().plusSeconds(expiration).toDate())
          .signWith(privateKey, SignatureAlgorithm.RS256)
          .compact();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getJwtPayload(String token) {
    try {
      PublicKey publicKey = JwtRas.parsePublicKey(PUBLIC_KEY);
      Jws<Claims> claimsJws = Jwts
              .parserBuilder()
              .setSigningKey(publicKey)
              .build()
              .parseClaimsJws(token);
      Claims body = claimsJws.getBody();
      return Optional.ofNullable(body.get(JWT_PAYLOAD_KEY)).orElse("").toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static class JwtRas {

    public static PrivateKey parsePrivateKey(String key) throws Exception {
      byte[] keyBytes = Base64.getDecoder().decode(key);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey parsePublicKey(String key) throws Exception {
      byte[] keyBytes = Base64.getDecoder().decode(key);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePublic(keySpec);
    }
  }
}

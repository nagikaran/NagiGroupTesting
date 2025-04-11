package com.NagiGroup.utility;
import java.util.Base64;

import io.jsonwebtoken.security.Keys;
public class SecretKeyGenerator {
	public static void main(String[] args) {
        String secretKey = Base64.getEncoder().encodeToString(Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded());
        System.out.println("Generated Secret Key: " + secretKey);
    }
}

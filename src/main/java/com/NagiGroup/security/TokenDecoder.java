package com.NagiGroup.security;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.stereotype.Component;

import com.NagiGroup.dto.LicenceKeyDto;
import com.NagiGroup.dto.UserDetailsDto;
import com.NagiGroup.dto.UserDto;
import com.NagiGroup.service.LicenceKeyService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class TokenDecoder {

    private static  LicenceKeyService licenceKeyService;

    public TokenDecoder(LicenceKeyService licenceKeyService) {
        this.licenceKeyService = licenceKeyService;
    }

    // Generate and return the RSA public key dynamically
    private static PublicKey getSigningKey() {
        try {
            LicenceKeyDto licenceKey = licenceKeyService.getLicenceKey();
            String publicKeyPem = licenceKey.getPublic_key();

            // Remove PEM headers and newlines
            publicKeyPem = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----", "")
                                       .replace("-----END PUBLIC KEY-----", "")
                                       .replaceAll("\\s+", "");

            // Decode the Base64 public key
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyPem);

            // Convert into RSA Public Key
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);

        } catch (Exception e) {
            throw new RuntimeException("Failed to decode public key: " + e.getMessage(), e);
        }
    }

    public static UserDetailsDto getUserDtoDataFromToken(String jwtToken) {
        try {
            if (jwtToken == null || jwtToken.trim().isEmpty()) {
                throw new JwtException("JWT token is missing or empty.");
            }

            PublicKey key = getSigningKey(); // Get the RSA public key dynamically
            System.out.println("Using Public Key: " + key); // Debugging log

            // Parse the JWT token using the RSA public key
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);
            Claims claims = claimsJws.getBody();

            System.out.println("Decoded JWT Claims: " + claims);

            // Map claims to UserDto
            UserDetailsDto userDto = new UserDetailsDto();
            userDto.setUser_id(claims.get("user_id", Integer.class));
            userDto.setRole_id(claims.get("role_id", Integer.class));
            userDto.setRole_name(claims.get("role_name", String.class)); // Fix: Now setting role_name
            userDto.setUser_name(claims.get("user_name", String.class)); // Fix: Ensuring user_name is set correctly
            userDto.setEmail_id(claims.get("email_id", String.class));
            //userDto.setContact_no(claims.get("contact_no", Integer.class));
System.out.println("userDto: "+userDto);

            return userDto;

        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token: " + e.getMessage());
        }
    }
}

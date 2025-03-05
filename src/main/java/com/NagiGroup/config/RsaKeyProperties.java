package com.NagiGroup.config;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.NagiGroup.dto.LicenceKeyDto;
import com.NagiGroup.service.LicenceKeyService;

@Configuration
public class RsaKeyProperties {

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private final LicenceKeyService licenceKeyService;

    public RsaKeyProperties(LicenceKeyService licenceKeyService) {
        this.licenceKeyService = licenceKeyService;
    }

    @PostConstruct
    private void loadKeys() throws Exception {
        LicenceKeyDto licenceKey = licenceKeyService.getLicenceKey();
        
        if (licenceKey == null || licenceKey.getPublic_key() == null || licenceKey.getPrivate_key() == null) {
            throw new IllegalStateException("Public or Private Key not found in the database.");
        }

        this.publicKey = loadPublicKey(licenceKey.getPublic_key());
        this.privateKey = loadPrivateKey(licenceKey.getPrivate_key());
    }

    private RSAPublicKey loadPublicKey(String publicKeyPEM) throws Exception {
        // Clean up the public key
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
                                   .replace("-----END PUBLIC KEY-----", "")
                                   .replaceAll("\\s+", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    private RSAPrivateKey loadPrivateKey(String privateKeyPEM) throws Exception {
        // Clean up the private key
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "")
                                     .replace("-----END PRIVATE KEY-----", "")
                                     .replaceAll("\\s+", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }
}

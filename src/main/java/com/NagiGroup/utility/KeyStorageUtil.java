package com.NagiGroup.utility;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import com.NagiGroup.repository.LicenceKeyRepository;
public class KeyStorageUtil {
	 public static String encodeFileToBase64(String filePath) throws Exception {
	        byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
	        return Base64.getEncoder().encodeToString(keyBytes);
	    }

	    public static void main(String[] args) throws Exception {
	        String privateKeyBase64 = encodeFileToBase64("src/main/resources/certs/private.pem");
	        String publicKeyBase64 = encodeFileToBase64("src/main/resources/certs/public.pem");

	        System.out.println("Private Key (Base64): " + privateKeyBase64);
	        System.out.println("Public Key (Base64): " + publicKeyBase64);
	       
	        
	        
	    }
}

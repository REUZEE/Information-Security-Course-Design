package com.pan1.config;

import com.pan1.utils.RSA;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class KeyRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RSA.keyPair = RSA.getKeyPair();
        System.out.println("private key: " + RSA.getPrivateKey(RSA.keyPair));
        System.out.println("public key: " + RSA.getPublicKey(RSA.keyPair));
    }
}

package com.movie.movieapp.security;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.movie.movieapp.Configuration;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


@Service
public class EncryptedKeysService {

    @Value("${jwt.cipherblock}")
    private String cipherBlock;

    @Value("${kms.keyId}")
    private String kmsArn;

    @Getter
    private String secret;

    @PostConstruct
    public void postConstruct(){
        AWSKMS kmsClient = Configuration.buildAWSClients(AWSKMSClientBuilder.standard());

        String [] arr = cipherBlock.split(",");
        byte[]hb = new byte[arr.length];
        for(int i =0;i<arr.length;i++){
            hb[i] = Byte.parseByte(arr[i]);
        }
        ByteBuffer b = ByteBuffer.wrap(hb,0,hb.length);
        DecryptRequest dec = new DecryptRequest().withCiphertextBlob(b).withKeyId(kmsArn);
        ByteBuffer plainText = kmsClient.decrypt(dec).getPlaintext();
        this.secret = StandardCharsets.UTF_8.decode(plainText).toString();
    }

    public ByteBuffer encrypt(String plainText){
        ByteBuffer plaintext = ByteBuffer.wrap(plainText.getBytes());
        AWSKMS kmsClient = Configuration.buildAWSClients(AWSKMSClientBuilder.standard());
        EncryptRequest req = new EncryptRequest().withKeyId(kmsArn).withPlaintext(plaintext);
        return kmsClient.encrypt(req).getCiphertextBlob();
    }
}

package com.movie.movieapp;

import com.amazonaws.client.builder.AwsSyncClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import org.json.JSONObject;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class Configuration {
    public static Properties getStoreProperties(String secretName) {

        AWSSecretsManager client = buildAWSClients(AWSSecretsManagerClientBuilder.standard());


        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
            JSONObject jo= new JSONObject(getSecretValueResult.getSecretString());
           // System.out.println(jo.toMap());


            Properties props=new Properties();
            jo.toMap().forEach((k,v)->{
                props.setProperty(k,String.valueOf(v));
               // System.out.println(k+"="+String.valueOf(v));
            });
            return props;
        } catch (Exception e) {
            throw e;
        }
    }

    public static <T> T buildAWSClients(AwsSyncClientBuilder builder) {
        //if (System.getProperty("local") != null || System.getProperty("app_local")!=null) {
//            return (T) builder.withCredentials(new ProfileCredentialsProvider())
//                    .withRegion(Regions.US_EAST_1)
//                    .build();
        //} else {
            return (T) builder.withRegion(Regions.US_EAST_1).build();
        //}
    }
}

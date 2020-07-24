package com.safetech.jobtask.service2.subscriber;

import com.safetech.jobtask.service2.model.RandomBytes;
import com.safetech.jobtask.service2.publisher.SignatureBuilder;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.security.*;

@Service
@Qualifier("subscriber")
public class MessageSubscriber implements MessageListener {

    private Jedis jedis;

    @PostConstruct
    public void setUp() {
        jedis = new Jedis("localhost", 6379, 2000);
        jedis.connect();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        JSONArray array = new JSONArray(message.toString());
        RandomBytes arr = new RandomBytes();
        arr.setRandomBytes(array);

        SignatureBuilder signatureBuilder = new SignatureBuilder();
        try {
            String signature = signatureBuilder.buildSignature(arr);
            jedis.set("signature", signature);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException
                | UnsupportedEncodingException | SignatureException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }
}
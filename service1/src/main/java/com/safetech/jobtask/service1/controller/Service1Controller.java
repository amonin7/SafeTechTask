package com.safetech.jobtask.service1.controller;

import com.safetech.jobtask.service1.model.RandomBytes;
import com.safetech.jobtask.service1.publisher.MessagePublisherImpl;
import com.safetech.jobtask.service1.subscriber.SignatureVerifier;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

@Controller
public class Service1Controller {

    private Jedis jedis;

    @PostConstruct
    public void setUp() {
        jedis = new Jedis("localhost", 6379, 2000);
        jedis.connect();
    }

    @Autowired
    MessagePublisherImpl messagePublisher;

    @RequestMapping(value = "/publish", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<CustomResponseEntity> publish() {
        RandomBytes array = new RandomBytes();
        array.generateArray();
        messagePublisher.publish(array.getJSONStringFromArray());
        Pipeline p = jedis.pipelined();
        Response<String> r2;
        String signature = null;
        while (signature == null) {
            r2 = p.get("signature");
            p.close();
            signature = r2.get();
        }
        jedis.del("signature");
        boolean result;
        try {
            result = new SignatureVerifier().verify(new JSONObject(signature));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException |
                UnsupportedEncodingException | SignatureException e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new CustomResponseEntity("Verification didn't pass with error: " + e.getMessage()),
                    HttpStatus.BAD_GATEWAY);
        }
        if (result)
            return new ResponseEntity<>(
                    new CustomResponseEntity("Verification passed successfully"),
                    HttpStatus.OK);
        else
            return new ResponseEntity<>(
                    new CustomResponseEntity("Verification haven't been passed"),
                    HttpStatus.CONFLICT);
    }

}

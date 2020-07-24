package com.safetech.jobtask.service1.subscriber;

import com.safetech.jobtask.service1.model.RandomBytes;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SignatureVerifier {

    public boolean verify(JSONObject obj) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException {

        Signature ecdsaVerify = Signature.getInstance(obj.getString("algorithm"));
        KeyFactory kf = KeyFactory.getInstance("EC");

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(obj.getString("publicKey")));

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        ecdsaVerify.initVerify(publicKey);
        RandomBytes bytes = new RandomBytes();
        bytes.setRandomBytes(new JSONArray(obj.getString("message")));
        ecdsaVerify.update(bytes.getRandomBytes());
        boolean result = ecdsaVerify.verify(Base64.getDecoder().decode(obj.getString("signature")));

        return result;
    }

}

package com.safetech.jobtask.service2.publisher;

import com.safetech.jobtask.service2.model.RandomBytes;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import static org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;

public class SignatureBuilder {

    private static final String SPEC = "prime256v1";
    private static final String ALGO = "SHA256withECDSA";

    public String buildSignature(RandomBytes bytes) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException, SignatureException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec(SPEC);

        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", PROVIDER_NAME);
        g.initialize(parameterSpec);
        KeyPair keypair = g.generateKeyPair();
        PublicKey publicKey = keypair.getPublic();
        PrivateKey privateKey = keypair.getPrivate();

        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(bytes.getRandomBytes());
        byte[] signature = ecdsaSign.sign();
        String pub = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String sig = Base64.getEncoder().encodeToString(signature);

        JSONObject obj = new JSONObject();
        obj.put("publicKey", pub);
        obj.put("signature", sig);
        obj.put("message", bytes.getJSONStringFromArray());
        obj.put("algorithm", ALGO);

        return obj.toString();
    }
}

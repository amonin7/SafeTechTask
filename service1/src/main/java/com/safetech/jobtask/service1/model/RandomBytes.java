package com.safetech.jobtask.service1.model;

import java.util.Random;
import org.json.JSONArray;

public class RandomBytes {

    private final byte[] randomBytes;

    /***
     * 200kb = 200 * 1024 byte = 204800 Bytes
     */
    private final int amountOfINTs = 204800;

    public RandomBytes() {
        this.randomBytes = new byte[amountOfINTs];
    }

    public void generateArray() {
        Random r = new Random();
        for (int i = 0; i < amountOfINTs; ++i) {
            randomBytes[i] = (byte) r.nextInt();
        }
    }

    public String getJSONStringFromArray() {
        JSONArray arrayToConvert = new JSONArray(this.randomBytes);
        return arrayToConvert.toString();
    }

    public byte[] getRandomBytes() {
        return randomBytes;
    }

    public void setRandomBytes(JSONArray jsonArray) {
        for ( int i = 0; i < jsonArray.length(); i++ ){
            this.randomBytes[i] = (byte) jsonArray.getInt(i);
        }
    }
}
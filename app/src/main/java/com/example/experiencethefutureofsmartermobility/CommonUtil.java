package com.example.experiencethefutureofsmartermobility;

import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;

public class CommonUtil {
    public static byte[] convertJsonObjectToBytes(JsonObject jsonObject) {
        String jsonString = jsonObject.toString();
        return jsonString.getBytes(StandardCharsets.UTF_8);
    }

}

package com.fork52.rxcodeforces.api;

import com.fork52.rxcodeforces.api.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Class representing a Codeforces Authenticator
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class CFAuthenticator {
    private String apiKey;
    private String apiSecret;
    private static final  String TOKENS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
    private static final Random random = new Random();

    /**
     * Generates and returns 6 characters random ID
     * */
    private static String generateRandomId(){
        char[] randomChars = new char[6];

        for(int i = 0; i < 6; i++){
            int randomIndex = random.nextInt(TOKENS.length());
            randomChars[i] = TOKENS.charAt(randomIndex);
        }
        return String.valueOf(randomChars);
    }

    public String getApiSignature(String methodName, List<Pair> queryParams){
        String randId = CFAuthenticator.generateRandomId();

        log.info("Query Params:" + this.getQueryString(queryParams));
        String hashKey = String.format("%s/%s?%s#%s" ,
                randId, methodName, this.getQueryString(queryParams), this.apiSecret);

        log.info("Hash Key:" + hashKey);
        return randId + CFAuthenticator.hashSHA512(hashKey);
    }

    /**
     * Returns a hashKey as defined by the codeforces-api
     * */
    private String getQueryString(List<Pair> queryParams){
        List<String> sortedParams = queryParams.stream()
                .sorted((p1, p2) -> {
                    if(!p1.getKey().equals(p2.getKey())) return p1.getKey().compareTo(p2.getKey());
                    else return p1.getValue().compareTo(p2.getValue());
                })
                .map(param -> String.format("%s=%s", param.getKey(), param.getValue()))
                .toList();
        return String.join("&", sortedParams);
    }

    /**
     * @param text String to be hashed.
     * @return Hashed String
     * Reference: <a href="http://www.java2s.com/example/java-utility-method/sha512/sha512-string-original-55397.html">SHA Hash</a>
     * */
    private static String hashSHA512(String text) {
        String sha512 = "";
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(text.getBytes());
            final byte[] digest = md.digest();
            final StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            sha512 = sb.toString();
        } catch (NoSuchAlgorithmException ignored) {
        }
        return sha512;
    }
}

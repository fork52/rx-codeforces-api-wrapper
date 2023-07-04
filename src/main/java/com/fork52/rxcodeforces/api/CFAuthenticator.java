package com.fork52.rxcodeforces.api;

import com.fork52.rxcodeforces.api.util.Pair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing a Codeforces Authenticator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class CFAuthenticator {

  private String apiKey;
  private String apiSecret;
  private static final String TOKENS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
  private static final Random random = new Random();

  /**
   * Generates and returns a random ID consisting of 6 characters.
   *
   * @return The generated random ID.
   */
  private static String generateRandomId() {
    char[] randomChars = new char[6];

    for (int i = 0; i < 6; i++) {
      int randomIndex = random.nextInt(TOKENS.length());
      randomChars[i] = TOKENS.charAt(randomIndex);
    }
    return String.valueOf(randomChars);
  }

  /**
   * Generates an API signature for the specified api-methodName and query parameters.
   *
   * @param methodName  The name of the API method.
   * @param queryParams The list of query parameters.
   * @return The generated API signature.
   */
  public String getApiSignature(String methodName, List<Pair> queryParams) {
    String randId = CFAuthenticator.generateRandomId();
    String hashKey = String.format("%s/%s?%s#%s",
        randId, methodName, this.getQueryString(queryParams), this.apiSecret);
    return randId + CFAuthenticator.hashSHA512(hashKey);
  }

  /**
   * Generates a query string by sorting the query parameters in ascending order based on key and
   * value.
   *
   * @param queryParams The list of query parameters.
   * @return The generated query string.
   */
  private String getQueryString(List<Pair> queryParams) {
    List<String> sortedParams = queryParams.stream()
        .sorted((p1, p2) -> {
          if (!p1.getKey().equals(p2.getKey())) {
            return p1.getKey().compareTo(p2.getKey());
          } else {
            return p1.getValue().compareTo(p2.getValue());
          }
        })
        .map(param -> String.format("%s=%s", param.getKey(), param.getValue()))
        .toList();
    return String.join("&", sortedParams);
  }

  /**
   * Returns the SHA512 hash of text
   *
   * @param text String to be hashed.
   * @return Hashed String
   */
  private static String hashSHA512(String text) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

      byte[] inputBytes = text.getBytes();
      byte[] hashBytes = messageDigest.digest(inputBytes);

      StringBuilder hashStringBuilder = new StringBuilder();
      for (byte b : hashBytes) {
        String hex = String.format("%02x", b);
        hashStringBuilder.append(hex);
      }

      return hashStringBuilder.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return null;
  }
}

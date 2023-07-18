package com.rxcodeforces.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a generic Codeforces Response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CFResponse<T> {

  private String status;
  private T result;
  private String comment;
}

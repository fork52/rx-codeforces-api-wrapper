package com.fork52.rxcodeforces.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a CF response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CFResponse<T> {

  private String status;
  private T result;
  private String comment;
}

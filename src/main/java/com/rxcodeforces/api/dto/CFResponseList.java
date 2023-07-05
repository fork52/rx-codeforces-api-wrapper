package com.rxcodeforces.api.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a CF response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CFResponseList<T> {

  private String status;
  private List<T> result;
  private String comment;
}

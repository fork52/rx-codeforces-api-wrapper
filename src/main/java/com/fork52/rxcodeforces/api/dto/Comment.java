package com.fork52.rxcodeforces.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a comment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

  /**
   * Integer.
   */
  private Integer id;

  /**
   * Integer. Time, when comment was created, in unix format.
   */
  private Integer creationTimeSecondsTime;

  /**
   * String.
   */
  private String commentatorHandle;

  /**
   * String.
   */
  private String locale;

  /**
   * String.
   */
  private String text;

  /**
   * Integer. Can be absent.
   */
  private Integer parentCommentId;

  /**
   * Integer.
   */
  private Integer rating;
}

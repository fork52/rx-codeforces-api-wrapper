package com.rxcodeforces.api.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents contest standings on codeforces.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestStandings {

  Contest contest;
  List<Problem> problems;
  List<RanklistRow> rows;
}

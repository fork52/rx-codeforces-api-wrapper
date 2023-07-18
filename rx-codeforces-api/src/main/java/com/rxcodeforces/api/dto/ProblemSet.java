package com.rxcodeforces.api.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a ProblemSet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemSet {

  List<Problem> problems;
  List<ProblemStatistics> problemStatistics;
}

package com.fork52.rxcodeforces.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

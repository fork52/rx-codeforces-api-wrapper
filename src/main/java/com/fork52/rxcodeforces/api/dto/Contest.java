package com.fork52.rxcodeforces.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contest {
    private Integer id;
    private String name;
    private String type;
    private String phase;
    private String frozen;
    private String durationSeconds;
    private String startTimeSeconds;
    private String preparedBy;
    private String websiteUrl;
    private String description;
    private String kind;
    private String season;
}
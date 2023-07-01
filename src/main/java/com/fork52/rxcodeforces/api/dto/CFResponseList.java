package com.fork52.rxcodeforces.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


/**
 * Represents a CF response.
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CFResponseList<T>{
    private String status;
    private List<T> result;
    private String comment;
}

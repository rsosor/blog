package com.rsosor.app.model.dto;

import com.rsosor.app.model.enums.Mode;
import lombok.Data;

/**
 * EnvironmentDTO
 *
 * @author RsosoR
 * @date 2021/9/3
 */
@Data
public class EnvironmentDTO {

    private String database;

    private Long startTime;

    private String version;

    private Mode mode;
}

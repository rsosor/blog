package com.rsosor.app.model.dto;

import lombok.Data;

/**
 * BackupDTO
 *
 * @author RsosoR
 * @date 2021/10/4
 */
@Data
public class BackupDTO {

    private String downloadLink;

    private String filename;

    private Long updateTime;

    private Long fileSize;
}

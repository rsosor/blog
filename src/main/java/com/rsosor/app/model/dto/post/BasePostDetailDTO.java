package com.rsosor.app.model.dto.post;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * BasePostDetailDTO
 *
 * @author RsosoR
 * @date 2021/10/4
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class BasePostDetailDTO extends BasePostSimpleDTO {

    private String originalContent;

    private String formatContent;

    private Long commentCount;
}

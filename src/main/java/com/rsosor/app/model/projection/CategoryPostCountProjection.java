package com.rsosor.app.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CategoryPostCountProjection
 *
 * @author RsosoR
 * @date 2021/9/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPostCountProjection {

    private Long postCount;

    private Integer categoryId;
}

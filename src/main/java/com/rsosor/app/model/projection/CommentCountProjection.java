package com.rsosor.app.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CommentCountProjection
 *
 * @author RsosoR
 * @date 2021/9/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCountProjection {

    private Long count;

    private Integer postId;
}

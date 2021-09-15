package com.rsosor.app.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CommentChildrenCountProjection
 *
 * @author RsosoR
 * @date 2021/9/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentChildrenCountProjection {

    private Long directChildrenCount;

    private Long commentId;
}

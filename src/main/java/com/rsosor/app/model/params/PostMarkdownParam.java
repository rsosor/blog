package com.rsosor.app.model.params;

import lombok.Data;

/**
 * PostMarkdownParam
 *
 * @author RsosoR
 * @date 2021/10/4
 */
@Data
public class PostMarkdownParam {

    /**
     * true if need frontMatter
     * default false
     */
    private Boolean needFrontMatter;
}

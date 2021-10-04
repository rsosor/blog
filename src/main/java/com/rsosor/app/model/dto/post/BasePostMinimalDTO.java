package com.rsosor.app.model.dto.post;

import com.rsosor.app.model.dto.base.IOutputConverter;
import com.rsosor.app.model.entity.BasePost;
import com.rsosor.app.model.enums.PostEditorType;
import com.rsosor.app.model.enums.PostStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * BasePostMinimalDTO
 *
 * @author RsosoR
 * @date 2021/10/4
 */
@Data
@ToString
@EqualsAndHashCode
public class BasePostMinimalDTO implements IOutputConverter<BasePostMinimalDTO, BasePost> {

    private Integer id;

    private String title;

    private PostStatus status;

    private String slug;

    private PostEditorType editorType;

    private Date updateTime;

    private Date createTime;

    private Date editTime;

    private String metaKeywords;

    private String metaDescription;

    private String fullPath;
}

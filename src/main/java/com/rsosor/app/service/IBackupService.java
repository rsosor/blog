package com.rsosor.app.service;

import com.rsosor.app.model.dto.post.BasePostDetailDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * IBackupService
 *
 * @author RsosoR
 * @date 2021/10/4
 */
public interface IBackupService {

    /**
     * Import markdown content.
     *
     * @param file file
     * @return base post detail dto
     * @throws IOException throws IOException
     */
    BasePostDetailDTO importMarkdown(MultipartFile file) throws IOException;
}

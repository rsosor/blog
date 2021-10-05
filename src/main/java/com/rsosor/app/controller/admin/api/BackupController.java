package com.rsosor.app.controller.admin.api;

import com.rsosor.app.annotation.DisableOnCondition;
import com.rsosor.app.config.properties.RsosoRProperties;
import com.rsosor.app.exception.NotFoundException;
import com.rsosor.app.model.dto.BackupDTO;
import com.rsosor.app.model.dto.post.BasePostDetailDTO;
import com.rsosor.app.model.params.PostMarkdownParam;
import com.rsosor.app.service.IBackupService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static com.rsosor.app.service.IBackupService.BackupType.*;

/**
 * BackupController
 *
 * @author RsosoR
 * @date 2021/10/4
 */
@RestController
@RequestMapping("/api/admin/backups")
@Slf4j
public class BackupController {

    private final IBackupService backupService;

    private final RsosoRProperties rsosorProperties;

    public BackupController(IBackupService backupService, RsosoRProperties rsosorProperties) {
        this.backupService = backupService;
        this.rsosorProperties = rsosorProperties;
    }

    @GetMapping("work-dir/fetch")
    public BackupDTO getWorkDirBackup(@RequestParam("filename") String filename) {
        return backupService
                .getBackup(Paths.get(rsosorProperties.getBackupDir(), filename), WHOLE_SITE)
                .orElseThrow(() ->
                        new NotFoundException("備份文件 " + filename + " 不存在或已刪除！").setErrorData(filename));
    }

    @GetMapping("data/fetch")
    public BackupDTO getDataBackup(@RequestParam("filename") String filename) {
        return backupService
                .getBackup(Paths.get(rsosorProperties.getDataExportDir(), filename), JSON_DATA)
                .orElseThrow(() ->
                        new NotFoundException("備份文件 " + filename + " 不存在或已刪除！").setErrorData(filename));
    }

    @GetMapping("markdown/fetch")
    public BackupDTO getMarkdownBackup(@RequestParam("filename") String filename) {
        return backupService
                .getBackup(Paths.get(rsosorProperties.getBackupMarkdownDir(), filename), MARKDOWN)
                .orElseThrow(() ->
                        new NotFoundException("備份文件 " + filename + " 不存在或已刪除！").setErrorData(filename));
    }

    @PostMapping("work-dir")
    @ApiOperation("Backups work directory")
    @DisableOnCondition
    public BackupDTO backupRsosoR() {
        return backupService.backupWorkDirectory();
    }

    @GetMapping("work-dir")
    @ApiOperation("Gets all work directory backups")
    public List<BackupDTO> listBackups() {
        return backupService.listWorkDirBackups();
    }

    @GetMapping("work-dir/{filename:.+}")
    @ApiOperation("Downloads a work directory backup file")
    @DisableOnCondition
    public ResponseEntity<Resource> downloadBackup(@PathVariable("filename") String filename,
                                                   HttpServletRequest request) {
        log.info("Trying to download backup file: [{}]", filename);

        // Load file as resource
        Resource backupResource =
                backupService.loadFileAsResource(rsosorProperties.getBackupDir(), filename);

        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        // Try to determine file's content type
        try {
            contentType =
                    request.getServletContext().getMimeType(backupResource.getFile().getAbsolutePath());
        } catch (IOException e) {
            log.warn("Could not determine file type", e);
            // Ignore this error
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + backupResource.getFilename() + "\"")
                .body(backupResource);
    }

    @DeleteMapping("work-dir")
    @ApiOperation("Deletes a work directory backup")
    @DisableOnCondition
    public void deleteBackup(@RequestParam("filename") String filename) {
        backupService.deleteWorkDirBackup(filename);
    }

    @PostMapping("markdown/import")
    @ApiOperation("Imports markdown")
    public BasePostDetailDTO backupMarkdowns(@RequestPart("file") MultipartFile file)
        throws IOException {
        return backupService.importMarkdown(file);
    }

    @PostMapping("data")
    @ApiOperation("Exports all data")
    @DisableOnCondition
    public BackupDTO exportData() {
        return backupService.exportData();
    }

    @GetMapping("data")
    @ApiOperation("Lists all exported data")
    public List<BackupDTO> listExportedData() {
        return backupService.listExportedData();
    }

    @DeleteMapping("data")
    @ApiOperation("Deletes a exported data")
    @DisableOnCondition
    public void deleteExportedData(@RequestParam("filename") String filename) {
        backupService.deleteExportedData(filename);
    }

    @GetMapping("data/{fileName:.+}")
    @ApiOperation("Downloads a exported data")
    @DisableOnCondition
    public ResponseEntity<Resource> downloadExportedData(@PathVariable("fileName") String filename,
                                                         HttpServletRequest request) {
        log.info("Try to download exported data file: [{}]", filename);

        // Load exported data as resource
        Resource exportDataResource =
                backupService.loadFileAsResource(rsosorProperties.getDataExportDir(),filename);

        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        // Try to determine file's content type
        try {
            contentType = request.getServletContext()
                    .getMimeType(exportDataResource.getFile().getAbsolutePath());
        } catch (IOException e) {
            log.warn("Could not determine file type", e);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + exportDataResource.getFilename() + "\"")
                .body(exportDataResource);
    }

    @PostMapping("markdown/export")
    @ApiOperation("Exports markdowns")
    @DisableOnCondition
    public BackupDTO exportMarkdowns(@RequestBody PostMarkdownParam postMarkdownParam)
        throws IOException {
        return backupService.exportMarkdowns(postMarkdownParam);
    }

    @GetMapping("markdown/export")
    @ApiOperation("Gets all markdown backups")
    public List<BackupDTO> listMarkdowns() {
        return backupService.listMarkdowns();
    }

    @DeleteMapping("markdown/export")
    @ApiOperation("Deletes a markdown backup")
    @DisableOnCondition
    public void deleteMarkdown(@RequestParam("filename") String filename) {
        backupService.deleteMarkdown(filename);
    }

    @GetMapping("markdown/export/{filename:.+}")
    @ApiOperation("Downloads a work markdown backup file")
    @DisableOnCondition
    public ResponseEntity<Resource> downloadMarkdown(@PathVariable("filename") String filename,
                                                     HttpServletRequest request) {
        log.info("Try to download markdown backup file: [{}]", filename);

        // Load file as resource
        Resource backupResource =
                backupService.loadFileAsResource(rsosorProperties.getBackupMarkdownDir(), filename);

        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        // Try to determine file's content type
        try {
            contentType =
                    request.getServletContext().getMimeType(backupResource.getFile().getAbsolutePath());
        } catch (IOException e) {
            log.warn("Could not determine file type", e);
            // Ignore this error
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + backupResource.getFilename() + "\"")
                .body(backupResource);
    }


}

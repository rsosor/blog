package com.rsosor.app.controller.admin.api;

import com.rsosor.app.config.properties.RsosoRProperties;
import com.rsosor.app.service.IBackupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}

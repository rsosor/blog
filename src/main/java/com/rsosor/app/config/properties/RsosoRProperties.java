package com.rsosor.app.config.properties;

import com.rsosor.app.model.enums.Mode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

import static com.rsosor.app.model.support.RsosoRConst.*;
import static com.rsosor.app.utils.RsosoRUtils.ensureSuffix;

/**
 * RsosoR configuration properties.
 *
 * @author RsosoR
 * @date 2021/9/16
 */
@Data
@ConfigurationProperties("rsosor")
public class RsosoRProperties {

    /**
     * Authentication enabled.
     */
    private boolean authEnabled = true;

    /**
     * RsosoR startup mode.
     */
    private Mode mode = Mode.PRODUCTION;

    /**
     * Admin path.
     */
    private String adminPath = "admin";

    /**
     * Work directory.
     */
    private String workDir = ensureSuffix(USER_HOME, FILE_SEPARATOR) + ".rsosor" + FILE_SEPARATOR;

    /**
     * RsosoR backup directory.(Not recommended to modify has this config);
     */
    private String backupDir =
            ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "rsosor-backup" + FILE_SEPARATOR;

    /**
     * RsosoR backup markdown directory.(Not recommended to modify this config);
     */
    private String backupMarkdownDir =
            ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "rsosor-backup-markdown" + FILE_SEPARATOR;

    /**
     * RsosoR data export directory.
     */
    private String dataExportDir =
            ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "rsosor-data-export" + FILE_SEPARATOR;

    /**
     * Upload prefix.
     */
    private String uploadUrlPrefix = "upload";

    /**
     * Download Timeout.
     */
    private Duration downloadTimeout = Duration.ofSeconds(30);

    /**
     * cache store impl
     * memory
     * level
     */
    private String cache = "memory";
}

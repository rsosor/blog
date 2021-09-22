package com.rsosor.app.event.logger;

import com.rsosor.app.model.enums.LogType;
import com.rsosor.app.model.params.LogParam;
import com.rsosor.app.utils.ServletUtils;
import com.rsosor.app.utils.ValidationUtils;
import org.springframework.context.ApplicationEvent;

/**
 * LogEvent
 *
 * @author RsosoR
 * @date 2021/9/23
 */
public class LogEvent extends ApplicationEvent {

    private final LogParam logParam;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     * @param logParam login param
     */
    public LogEvent(Object source, LogParam logParam) {
        super(source);

        // Validate the log param
        ValidationUtils.validate(logParam);

        // Set ip address
        logParam.setIpAddress(ServletUtils.getRequestIp());

        this.logParam = logParam;
    }

    public LogEvent(Object source, String logKey, LogType logType, String content) {
        this(source, new LogParam(logKey, logType, content));
    }

    public LogParam getLogParam() {
        return logParam;
    }
}

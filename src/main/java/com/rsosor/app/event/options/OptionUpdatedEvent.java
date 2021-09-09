package com.rsosor.app.event.options;

import org.springframework.context.ApplicationEvent;

/**
 * OptionUpdatedEvent
 *
 * @author RsosoR
 * @date 2021/9/9
 */
public class OptionUpdatedEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public OptionUpdatedEvent(Object source) {
        super(source);
    }
}

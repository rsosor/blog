package com.rsosor.app.security.context;

import com.rsosor.app.security.authentication.IAuthentication;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * SecurityContextImpl
 *
 * @author RsosoR
 * @date 2021/9/25
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SecurityContextImpl implements ISecurityContext {

    private IAuthentication authentication;

    @Override
    public IAuthentication getAuthentication() {
        return authentication;
    }

    @Override
    public void setAuthentication(IAuthentication authentication) {
        this.authentication = authentication;
    }
}

package com.rsosor.app.model.entity.support;

import com.rsosor.app.utils.ReflectionUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;

/**
 * CustomIdGenerator
 *
 * @author RsosoR
 * @date 2021/9/16
 */
public class CustomIdGenerator extends IdentityGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Object id = ReflectionUtils.getFieldValue("id", object);
        if (id != null) {
            return (Serializable) id;
        }
        return super.generate(session, object);
    }
}

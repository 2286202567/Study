package org.springboot;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

public class TestOnCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        try {
            MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(TestConditionOnClass.class.getName());
            context.getClassLoader().loadClass(attributes.values().stream().findFirst().get().toString());
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}

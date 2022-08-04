package io.hcbm.starter.web.annotation;

import java.lang.annotation.*;

/**
 * Tenant
 *
 * @author David 2022/03/29 14:25
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Tenant {

    String[] value() default {"default"};

}
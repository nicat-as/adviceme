package com.uniso.equso.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@NotNull
@Size(min = 5, max = 20, message = "{exception.password-length}")
@Constraint(validatedBy = {}) // constraints composition
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface WeakPassword {

    String message() default "Password doesn't match bean validation constraints.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
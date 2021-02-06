package com.uniso.equso.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@NotNull
@Size(min=8, max=20,message = "{exception.password-length}")
@Pattern.List({
        @Pattern(regexp = "(?=.*[0-9]).+", message = "{exception.password-number}"),
        @Pattern(regexp = "(?=.*[a-z]).+", message = "{exception.password-lower-case}"),
        @Pattern(regexp = "(?=.*[A-Z]).+", message = "{exception.password-upper-case}"),
        @Pattern(regexp = "(?=.*\\W).+", message ="{exception.password-special-character}"),
        @Pattern(regexp = "(?=\\S+$).+", message = "{exception.password-whitespace}")
})
@Constraint(validatedBy = {}) // constraints composition
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {

    String message() default "Password doesn't match bean validation constraints.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
package group.int221project.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DisplayValidator.class)
@Documented
public @interface ValidDisplay {

        String message() default "Invalid display";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
}

package morkato.api.infra.validation;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.constraints.Min;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Min(value = 0)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME) 
@Documented
@Constraint(validatedBy = {})
public @interface AttrSchema {
  String message() default "This attribute is invalid!";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}

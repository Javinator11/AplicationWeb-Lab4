package mx.tec.web.lab.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Interface for validating size for SkuVO
 * @author Javier Sosa Puga
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Constraint(validatedBy = SizeValidator.class)
public @interface ValidSize {
	/**
	 * 
	 * @return default message
	 */
	String message() default "Invalid size";

	/**
	 * 
	 * @return default group
	 */
	Class<?>[] groups() default {};

	/**
	 * 
	 * @return default payload
	 */
	Class<? extends Payload>[] payload() default {};
}

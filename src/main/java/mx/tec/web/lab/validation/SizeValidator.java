package mx.tec.web.lab.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The Size Validator with everything to verify size
 * @author Javier Sosa Puga
 *
 */
public class SizeValidator implements ConstraintValidator<ValidSize, String> {
	private final List<String> validSizes = Arrays.asList("XS", "S" , "M", "L", "XL");

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return validSizes.contains(value);
	}
}

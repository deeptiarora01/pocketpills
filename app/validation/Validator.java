package validation;

import java.io.IOException;

/**
 * Interface to validate object of type T.
 * 
 * @author deeptiarora
 *
 * @param <T>
 */
public interface Validator<T> {
	
		boolean isValid(T object) throws IOException;
}

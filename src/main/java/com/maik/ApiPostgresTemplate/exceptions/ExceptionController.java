package com.maik.ApiPostgresTemplate.exceptions;

import com.maik.ApiPostgresTemplate.models.dto.ErrorDTO;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

/**
 * The Class ExceptionController.
 */
@ControllerAdvice
public class ExceptionController {
	
	/**
	 * Generic exception.
	 * 
	 * A qualified exception shall be thrown when it receives an exception that has not been caught by one of the other methods.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(value = Throwable.class)
	public ResponseEntity<ErrorDTO> genericException(Throwable e) {

		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		errorDTO.setError(e.getMessage());

		return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	/**
	 * Api exception.
	 *
	 * Throws an exception when it receives an ApiException object.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(value = ApiException.class)
	public ResponseEntity<ErrorDTO> apiException(ApiException e) {

		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setCode(HttpStatus.valueOf(e.getCode()).toString());
		errorDTO.setError(e.getMessage());
		return new ResponseEntity<>(errorDTO, HttpStatus.valueOf(e.getCode()));

	}
	

	/**
	 * Element not found exception.
	 * 
	 * Throws an exception when it fails to retrieve an element.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(value = NoSuchElementException.class)
	public ResponseEntity<ErrorDTO> elementNotFoundException(NoSuchElementException e) {

		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setCode(HttpStatus.NOT_FOUND.toString());
		errorDTO.setError("Element not found");

		return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);

	}
	

	/**
	 * Bad request exception.
	 * 
	 * Throw an exception when parameters are not reported correctly.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class,
			MethodArgumentNotValidException.class, IllegalArgumentException.class, InvalidDataAccessApiUsageException.class})
	public ResponseEntity<ErrorDTO> badRequestException(Exception e) {

		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setCode(HttpStatus.BAD_REQUEST.toString());
		errorDTO.setError(e.getMessage());

		return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);

	}


	/**
	 * Bad credentials exception.
	 *
	 * Throws exception when credentials do not match any registered user
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler({BadCredentialsException.class})
	public ResponseEntity<ErrorDTO> badCredentialsException(BadCredentialsException e) {

		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setCode(HttpStatus.UNAUTHORIZED.toString());
		errorDTO.setError("Invalid credentials");

		return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);

	}

	/**
	 * Access denied exception.
	 *
	 * Throws exception when user has no permission to access the resource
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler({AccessDeniedException.class})
	public ResponseEntity<ErrorDTO> accessDeniedException(AccessDeniedException e) {

		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setCode(HttpStatus.UNAUTHORIZED.toString());
		errorDTO.setError("Access denied");

		return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);

	}


}

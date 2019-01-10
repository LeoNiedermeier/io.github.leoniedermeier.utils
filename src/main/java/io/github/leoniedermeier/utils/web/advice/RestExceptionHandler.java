package io.github.leoniedermeier.utils.web.advice;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.ReflectionUtils.findField;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.leoniedermeier.utils.excecption.ContextedRuntimeException;
import io.github.leoniedermeier.utils.excecption.ErrorCode;

@RestControllerAdvice
public class RestExceptionHandler {

	public static final String HEADER_CID = "X-HEADER_CID";
	public static final String HEADER_ERROR_CODES = "X-ERROR-CODES";

	public RestExceptionHandler() {
		super();
	}

	@ExceptionHandler(ContextedRuntimeException.class)
	public ResponseEntity<ErrorInformation> handle(ContextedRuntimeException exception) {

		ErrorInformation errorInformation = new ErrorInformation();
		exception.getCID().ifPresent(errorInformation::setCid);
		errorInformation.setErrroCodes(exception.getErrorCodes().map(ErrorCode::code).collect(toList()));

		// If the ErrorCode is an enum field, check whether it is annotated with HttpResponseStatus.
		errorInformation.setStatus(exception.findLastErrorCode()
				.filter(ec -> ec.getClass().isEnum())
				// (enum) field with same name as code() returns
				.map(ec -> findField(ec.getClass(), ec.code(), ec.getClass()))
				.map(field -> field.getAnnotation(HttpResponseStatus.class))//
				.map(HttpResponseStatus::value).orElse(HttpStatus.INTERNAL_SERVER_ERROR));

		return ResponseEntity.status(errorInformation.getStatus())
				.header(HEADER_ERROR_CODES, errorInformation.getErrroCodes().toArray(new String[0]))//
				.header(HEADER_CID, errorInformation.getCid())//
				.body(errorInformation);
	}
}

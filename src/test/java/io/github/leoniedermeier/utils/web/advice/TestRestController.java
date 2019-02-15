package io.github.leoniedermeier.utils.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.leoniedermeier.utils.excecption.ContextedRuntimeException;
import io.github.leoniedermeier.utils.excecption.EnumErrorCode;

@org.springframework.web.bind.annotation.RestController
class TestRestController {


	public TestRestController() {
		super();
	}

	@RequestMapping("/text")
	public String text() {
		return "TEXT";
	}

	@RequestMapping("/exception")
	public String exception() {
		throw new ContextedRuntimeException(MyErrorCodes.ERROR_CODE_SIMPLE);
	}

	@RequestMapping("/exception_with_status")
	public String exception_with_status() {
		throw new ContextedRuntimeException(MyErrorCodes.ERROR_CODE_WITH_HTTP_NOT_FOUND);
	}

	enum MyErrorCodes implements EnumErrorCode {

		ERROR_CODE_SIMPLE,

		@HttpResponseStatus(HttpStatus.NOT_FOUND)
		ERROR_CODE_WITH_HTTP_NOT_FOUND;
	}
}

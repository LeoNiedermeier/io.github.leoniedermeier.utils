package io.github.leoniedermeier.utils.web.advice;

import static io.github.leoniedermeier.utils.test.web.advice.HeaderErrorCodeMockMvcResultMatchers.headerErrorCode;
import static io.github.leoniedermeier.utils.web.advice.RestExceptionHandler.HEADER_CID;
import static io.github.leoniedermeier.utils.web.advice.RestExceptionHandler.HEADER_ERROR_CODES;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.github.leoniedermeier.utils.web.advice.TestRestController.MyErrorCodes;

  class ExceptionHandlingTest {

	private MockMvc mockMvc;

	@BeforeEach
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(new TestRestController())
				.setControllerAdvice(new RestExceptionHandler()).build();

	}

	@Test
	public void text() throws Exception {

		mockMvc.perform(get("/text")).andExpect(status().isOk());
	}

	@Test
	public void exception() throws Exception {

		mockMvc.perform(get("/exception").accept(MediaType.APPLICATION_JSON))
				// .andDo(MockMvcResultHandlers.print())
				.andExpect(status().is5xxServerError())
				.andExpect(header().stringValues(HEADER_ERROR_CODES, MyErrorCodes.ERROR_CODE_SIMPLE.code()))
				.andExpect(header().stringValues(HEADER_CID, iterableWithSize(1)));
	}

	@Test
	public void exception_with_status() throws Exception {

		mockMvc.perform(get("/exception_with_status").accept(MediaType.APPLICATION_JSON))//
				// .andDo(MockMvcResultHandlers.print())//
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()))//
				.andExpect(headerErrorCode(MyErrorCodes.ERROR_CODE_WITH_HTTP_NOT_FOUND));
	}

 
}

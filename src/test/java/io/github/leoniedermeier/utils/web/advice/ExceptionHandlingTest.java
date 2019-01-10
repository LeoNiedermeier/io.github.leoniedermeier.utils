package io.github.leoniedermeier.utils.web.advice;

import static io.github.leoniedermeier.utils.web.advice.RestExceptionHandler.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.github.leoniedermeier.utils.web.advice.TestRestController.MyErrorCodes;

public class ExceptionHandlingTest {

	private MockMvc mockMvc;

	@BeforeEach
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(new TestRestController())
				.setControllerAdvice(new RestExceptionHandler()).build();

	}

	@Test
	public void test() throws Exception {

		mockMvc.perform(get("/text")).andExpect(status().isOk());
	}

	@Test
	public void exception() throws Exception {

		mockMvc.perform(get("/exception").accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().is5xxServerError())
				.andExpect(header().stringValues(HEADER_ERROR_CODES, MyErrorCodes.ERROR_CODE_SIMPLE.code()))
				.andExpect(header().stringValues(HEADER_CID, iterableWithSize(1)));
	}

	@Test
	public void exception_with_status() throws Exception {

		mockMvc.perform(get("/exception_with_status").accept(MediaType.APPLICATION_JSON))//
				// .andDo(MockMvcResultHandlers.print())//
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()))//
				.andExpect(
						header().stringValues(HEADER_ERROR_CODES, MyErrorCodes.ERROR_CODE_WITH_HTTP_NOT_FOUND.code()));
	}
}

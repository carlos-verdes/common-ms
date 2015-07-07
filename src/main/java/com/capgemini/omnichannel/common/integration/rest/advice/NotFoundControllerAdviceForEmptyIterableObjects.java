package com.capgemini.omnichannel.common.integration.rest.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * <p>
 * Advice to return 404 when a result is null.
 * <p>
 * This doesn't apply for Iterable objects which will use another advice.
 * 
 * @author cverdes
 *
 */
@ControllerAdvice
public class NotFoundControllerAdviceForEmptyIterableObjects implements ResponseBodyAdvice<Iterable<? extends Object>> {

	@Override
	public Iterable<? extends Object> beforeBodyWrite(Iterable<? extends Object> body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {

		// returns 404 for get methods and null results
		if (request.getMethod().equals(HttpMethod.GET) && (body == null || !body.iterator().hasNext())) {

			response.setStatusCode(HttpStatus.NOT_FOUND);
		}

		return body;
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

		boolean isIterable = returnType.getClass().isAssignableFrom(Iterable.class);

		return isIterable;
	}

}

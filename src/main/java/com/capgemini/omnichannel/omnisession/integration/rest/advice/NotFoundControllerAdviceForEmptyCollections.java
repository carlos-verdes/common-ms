package com.capgemini.omnichannel.omnisession.integration.rest.advice;

import java.util.Collection;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class NotFoundControllerAdviceForEmptyCollections implements ResponseBodyAdvice<Collection<? extends Object>> {

	@Override
	public Collection<? extends Object> beforeBodyWrite(Collection<? extends Object> body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {

		// 404 ERROR for when null or empty results
		if (request.getMethod().equals(HttpMethod.GET) && body == null || body.isEmpty()) {
			response.setStatusCode(HttpStatus.NOT_FOUND);
		}

		return body;
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

		return true;
	}

}

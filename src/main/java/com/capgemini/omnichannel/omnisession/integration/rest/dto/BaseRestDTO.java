package com.capgemini.omnichannel.omnisession.integration.rest.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

public class BaseRestDTO<T> extends ResourceSupport implements Serializable{
	private static final long serialVersionUID = 5726511815446236389L;
	
	private T data;

	public BaseRestDTO() {
		super();
	}

	public BaseRestDTO(T data) {
		this();
		this.data = data;
	}



	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
}

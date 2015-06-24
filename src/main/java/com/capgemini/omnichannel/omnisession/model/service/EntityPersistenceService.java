package com.capgemini.omnichannel.omnisession.model.service;

public interface EntityPersistenceService<T> {

	 Class<? extends T> getEntityClass();

	 T getEntityById(String id);

	 T updateEntity(String id, T value);

	 T insertEntity(String id, T value);

}

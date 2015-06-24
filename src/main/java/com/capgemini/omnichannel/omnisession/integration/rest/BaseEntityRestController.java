package com.capgemini.omnichannel.omnisession.integration.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capgemini.omnichannel.omnisession.integration.rest.dto.BaseRestDTO;
import com.capgemini.omnichannel.omnisession.model.service.EntityPersistenceService;

public abstract class BaseEntityRestController<T> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	abstract EntityPersistenceService<T> getEntityPersistenceService();
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody()
	public BaseRestDTO<T> getEntity(@PathVariable String id) {
		logger.debug("GET entity {} with id {}", getEntityPersistenceService().getEntityClass(), id);

		BaseRestDTO<T> result = null;
		T entity = getEntityPersistenceService().getEntityById(id);
		if (entity != null) {
			logger.debug("entity found: {}", entity);

			result = createRestDTO(entity);
		}

		logger.debug("result: {}", result);

		return result;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public BaseRestDTO<T> putEntity(@PathVariable String id, @RequestBody T value) {
		logger.debug("PUT entity id:entity: {}:{} ", id, value);

		BaseRestDTO<T> result = null;
		if (id != null && value != null) {
			T entity = getEntityPersistenceService().updateEntity(id, value);
			result = createRestDTO(entity);
		}

		return result;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public BaseRestDTO<T> updateSessionInfo(@PathVariable String id, @RequestBody T value) {
		logger.debug("POST entity id:entity: {}:{} ", id, value);

		BaseRestDTO<T> result = null;
		if (id != null && value != null) {
			T entity = getEntityPersistenceService().insertEntity(id, value);
			result = createRestDTO(entity);
		}

		return result;
	}

	private BaseRestDTO<T> createRestDTO(T entity) {
		BaseRestDTO<T> result;
		result = new BaseRestDTO<T>(entity);
		addLinksToEntity(entity);
		return result;
	}

	protected void addLinksToEntity(T entity) {

	}

}

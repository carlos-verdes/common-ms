package com.capgemini.omnichannel.omnisession.integration.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capgemini.omnichannel.omnisession.integration.rest.dto.BaseRestDTO;
import com.capgemini.omnichannel.omnisession.model.service.ResourcePersistenceService;

/**
 * Base class for rest resources with the next convention:
 * <ul>
 * <li> GET  /{id} --> retrieves a resource by id
 * <li> PUT  /{id} --> update a resource 
 * <li> POST /{id} --> insert a resource
 * 
 *  <p>The difference between PUT and POST is that the same PUT request executed more than once has always the same result (omni potent). 
 *  <p>
 *  <p>This class uses the interface {@link ResourcePersistenceService#updateResource(String, Object)} for PUT requests and {@link ResourcePersistenceService#insertResource(String, Object)}, so the developer could choose the behaviour of both requests.
 *  <p>One scenario could be a web with articles where we want to update an article in our catalog or to insert that article in a shopping cart. In this scenario the developer could choose PUT for article updates and POST to add a new item to the shopping cart.  
 * @author cverdesm
 *
 */
public abstract class BaseResourceRestController<Resource,ResourceID> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	abstract ResourcePersistenceService<Resource,ResourceID> getResourcePersistenceService();
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody()
	public BaseRestDTO<Resource> getEntity(@PathVariable ResourceID id) {
		logger.debug("GET entity {} with id {}", getResourcePersistenceService().getResourceClass(), id);

		BaseRestDTO<Resource> result = null;
		Resource entity = getResourcePersistenceService().getResourceById(id);
		if (entity != null) {
			logger.debug("entity found: {}", entity);

			result = createRestDTO(entity);
		}

		logger.debug("result: {}", result);

		return result;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public BaseRestDTO<Resource> putEntity(@PathVariable ResourceID id, @RequestBody Resource value) {
		logger.debug("PUT entity id:entity: {}:{} ", id, value);

		BaseRestDTO<Resource> result = null;
		if (id != null && value != null) {
			Resource entity = getResourcePersistenceService().updateResource(id, value);
			result = createRestDTO(entity);
		}

		return result;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public BaseRestDTO<Resource> updateSessionInfo(@PathVariable ResourceID id, @RequestBody Resource value) {
		logger.debug("POST entity id:entity: {}:{} ", id, value);

		BaseRestDTO<Resource> result = null;
		if (id != null && value != null) {
			Resource entity = getResourcePersistenceService().insertResource(id, value);
			result = createRestDTO(entity);
		}

		return result;
	}

	private BaseRestDTO<Resource> createRestDTO(Resource entity) {
		BaseRestDTO<Resource> result;
		result = new BaseRestDTO<Resource>(entity);
		addLinksToEntity(entity);
		return result;
	}

	protected void addLinksToEntity(Resource entity) {

	}

}

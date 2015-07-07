package com.capgemini.omnichannel.common.integration.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capgemini.omnichannel.common.integration.rest.dto.BaseRestDTO;
import com.capgemini.omnichannel.common.model.service.ResourcePersistenceService;

/**
 * Base class for rest resources with the next convention:
 * <ul>
 * <li>GET /{id} --> retrieves a resource by id
 * <li>PUT /{id} --> update a resource
 * <li>POST /{id} --> insert a resource
 * 
 * <p>
 * The difference between PUT and POST is that the same PUT request executed more than once has always the same result
 * (omni potent).
 * <p>
 * <p>
 * This class uses the interface {@link ResourcePersistenceService#updateResource(String, Object)} for PUT requests and
 * {@link ResourcePersistenceService#insertResource(String, Object)}, so the developer could choose the behaviour of
 * both requests.
 * <p>
 * One scenario could be a web with articles where we want to update an article in our catalog or to insert that article
 * in a shopping cart. In this scenario the developer could choose PUT for article updates and POST to add a new item to
 * the shopping cart.
 * 
 * @author cverdesm
 *
 */
public abstract class BaseResourceRestController<Resource> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	abstract protected ResourcePersistenceService<Resource> getResourcePersistenceService();

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody BaseRestDTO<List<BaseRestDTO<Resource>>> getResources(Principal principal) {
		logger.debug("GET / ... Controller:{}", this.getClass());

		BaseRestDTO<List<BaseRestDTO<Resource>>> result = null;
		List<Resource> resources = getResourcePersistenceService().getResources(principal);
		if (resources != null && !resources.isEmpty()) {
			logger.debug("resources found: {}", resources);

			result = createRestDTO(resources, principal);
		}

		logger.debug("result: {}", result);

		return result;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseRestDTO<Resource> getResource(@PathVariable String id, Principal principal) {
		
		logger.debug("GET /{} .. Controller:{}", id, this.getClass());

		BaseRestDTO<Resource> result = null;
		Resource resource = getResourcePersistenceService().getResourceById(id, principal);
		if (resource != null) {
			logger.debug("resource found: {}", resource);

			result = createRestDTO(resource, principal);
		}

		logger.debug("result: {}", result);

		return result;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public BaseRestDTO<Resource> putResource(@PathVariable String id, @RequestBody Resource value, Principal principal) {
		logger.debug("PUT /{} {} ", id, value);

		BaseRestDTO<Resource> result = null;
		if (id != null && value != null) {
			Resource resource = getResourcePersistenceService().updateResource(id, value, principal);
			result = createRestDTO(resource, principal);
		}

		return result;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public BaseRestDTO<Resource> updateSessionInfo(@PathVariable String id, @RequestBody Resource value,
			Principal principal) {
		logger.debug("POST /{} {} ", id, value);

		BaseRestDTO<Resource> result = null;
		if (id != null && value != null) {
			Resource resource = getResourcePersistenceService().insertResource(id, value, principal);
			result = createRestDTO(resource, principal);
		}

		return result;
	}

	private BaseRestDTO<List<BaseRestDTO<Resource>>> createRestDTO(Iterable<Resource> resources, Principal principal) {
		BaseRestDTO<List<BaseRestDTO<Resource>>> result = new BaseRestDTO<List<BaseRestDTO<Resource>>>(new ArrayList<BaseRestDTO<Resource>>());
		List<BaseRestDTO<Resource>> data = result.getData();

		// generate dtos
		for (Resource resource : resources) {
			BaseRestDTO<Resource> dto = createRestDTO(resource, principal);

			data.add(dto);

		}

		// add general links
		addLinksToResources(result);

		return result;

	}

	private BaseRestDTO<Resource> createRestDTO(Resource resource, Principal principal) {

		BaseRestDTO<Resource> result;
		result = new BaseRestDTO<Resource>(resource);

		// add self link
		addSelfAndOtherLinksToResource(resource, principal, result);

		return result;
	}

	protected void addSelfAndOtherLinksToResource(Resource resource, Principal principal, BaseRestDTO<Resource> result) {
		@SuppressWarnings("unchecked")
		BaseResourceRestController<Resource> controller = methodOn(this.getClass());
		String id = this.getResourcePersistenceService().getResourceId(resource);
		BaseRestDTO<Resource> method = controller.getResource(id, principal);
		ControllerLinkBuilder linkBuilder = linkTo(method);
		result.add(linkBuilder.withSelfRel());
		// add external links (from controller implementation)
		addLinksToResource(result);
	}

	protected void addLinksToResource(BaseRestDTO<Resource> result) {

	}

	protected void addLinksToResources(BaseRestDTO<List<BaseRestDTO<Resource>>> results) {

	}

}

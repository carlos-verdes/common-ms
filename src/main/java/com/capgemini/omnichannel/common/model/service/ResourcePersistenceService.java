package com.capgemini.omnichannel.common.model.service;

import java.security.Principal;
import java.util.List;

import com.capgemini.omnichannel.common.integration.rest.BaseResourceRestController;

/**
 * <p>
 * Interface used by {@link BaseResourceRestController} to retrieve/store resources.
 * 
 * @author cverdesm
 *
 * @param <Resource>
 * @param <String>
 */
public interface ResourcePersistenceService<Resource> {

	String getResourceId(Resource resource);

	List<Resource> getResources(Principal principal);

	Resource getResourceById(String id, Principal principal);

	<S extends Resource> S updateResource(String id, S value, Principal principal);

	<S extends Resource> S insertResource(String id, S value, Principal principal);

}

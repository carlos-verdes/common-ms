package com.capgemini.omnichannel.omnisession.model.service;

import com.capgemini.omnichannel.omnisession.integration.rest.BaseResourceRestController;

/**
 * <p>Interface used by {@link BaseResourceRestController} to retrieve/store resources.
 * @author cverdesm
 *
 * @param <Resource>
 * @param <ResourceID>
 */
public interface ResourcePersistenceService<Resource, ResourceID> {

	Class<? extends Resource> getResourceClass();

	Resource getResourceById(ResourceID id);

	<S extends Resource> S updateResource(ResourceID id, S value);

	<S extends Resource> S insertResource(ResourceID id, S value);

}

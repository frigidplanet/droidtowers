/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.server;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.happydroids.HappyDroidConsts;
import com.happydroids.droidtowers.gamestate.server.TowerGameService;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
@JsonFilter(value = "HappyDroidServiceObject")
public abstract class HappyDroidServiceObject {

	private long id;
	private String resourceUri;
	private boolean fetchError;

	@JsonIgnore
	public abstract String getBaseResourceUri();

	protected HappyDroidServiceObject() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getResourceUri() {
		return resourceUri;
	}

	protected abstract boolean requireAuthentication();

	public void setResourceUri(String resourceUri) {
		if (resourceUri != null && !resourceUri.startsWith("http")) {
			resourceUri = HappyDroidConsts.HAPPYDROIDS_URI + resourceUri;
		}

		this.resourceUri = resourceUri;
	}

	private void validateResourceUri() {
		if (resourceUri != null && !resourceUri.startsWith("http")) {
			resourceUri = HappyDroidConsts.HAPPYDROIDS_URI + resourceUri;
		}
	}

	protected int getCacheMaxAge() {
		return -1;
	}

	protected boolean isCachingAllowed() {
		return false;
	}

	public void save() {
		return; // wasn't doing anything anyway
	}

	private void copyValueFromField(HappyDroidServiceObject serverInstance, Field field) {
		try {
			if (!Modifier.isFinal(field.getModifiers())) {
				field.setAccessible(true);
				field.set(this, field.get(serverInstance));
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@JsonIgnore
	public boolean isSaved() {
		return resourceUri != null;
	}

	protected ObjectMapper getObjectMapper() {
		return TowerGameService.instance().getObjectMapper();
	}

	public void fetch() {
		return;
	}

	public boolean errorWhileFetching() {
		return fetchError;
	}
}

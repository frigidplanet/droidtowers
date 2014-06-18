/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers.gamestate.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.happydroids.HappyDroidConsts;
import com.happydroids.server.HappyDroidServiceObject;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
public class Device extends HappyDroidServiceObject {
	public String uuid;
	public String type;
	public String market;
	public String osVersion;
	public String appVersion;
	public final int appVersionCode;
	public boolean isAuthenticated;

	public Device() {
		uuid = TowerGameService.instance().getDeviceId();
		type = TowerGameService.getDeviceType();
		market = TowerGameService.getDeviceOSMarketName();
		osVersion = TowerGameService.getDeviceOSVersion();
		appVersion = HappyDroidConsts.VERSION;
		appVersionCode = HappyDroidConsts.VERSION_CODE;
	}

	@Override
	public String getBaseResourceUri() {
		return HappyDroidConsts.HAPPYDROIDS_URI + "/api/v1/register-device/";
	}

	@Override
	protected boolean requireAuthentication() {
		return false;
	}
}

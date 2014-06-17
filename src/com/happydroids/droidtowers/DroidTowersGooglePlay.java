/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers;

import android.util.DisplayMetrics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.happydroids.droidtowers.gamestate.server.TowerGameService;
import com.happydroids.droidtowers.platform.Display;
import com.happydroids.platform.AndroidBrowserUtil;
import com.happydroids.platform.AndroidDialogOpener;
import com.happydroids.platform.AndroidUncaughtExceptionHandler;
import com.happydroids.platform.Platform;
import com.happydroids.platform.PlatformConnectionMonitor;

public class DroidTowersGooglePlay extends AndroidApplication {
	private static final String TAG = DroidTowersGooglePlay.class.getSimpleName();
	
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		Display.setXHDPI(metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH);
		Display.setScaledDensity(metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH ? 1.5f
				: 1f);

		TowerGameService.setDeviceType("android");
		TowerGameService.setDeviceOSMarketName("google-play");
		TowerGameService.setDeviceOSVersion("sdk" + getVersion());

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useGL20 = true;
		config.useWakelock = true;
		
		initialize(new DroidTowersGame(new Runnable() {
			@Override
			public void run() {
				Platform.setDialogOpener(new AndroidDialogOpener(
						DroidTowersGooglePlay.this));
				Platform.setConnectionMonitor(new PlatformConnectionMonitor());
				Platform.setUncaughtExceptionHandler(new AndroidUncaughtExceptionHandler());
				Platform.setBrowserUtil(new AndroidBrowserUtil(
						DroidTowersGooglePlay.this));
			}
		}), config);

		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
	}

}

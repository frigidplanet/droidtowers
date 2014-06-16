/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers;

import net.robotmedia.billing.BillingController;
import net.robotmedia.billing.BillingRequest;
import net.robotmedia.billing.helper.AbstractBillingObserver;
import android.util.DisplayMetrics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.happydroids.HappyDroidConsts;
import com.happydroids.droidtowers.gamestate.server.TowerGameService;
import com.happydroids.droidtowers.platform.Display;
import com.happydroids.platform.AndroidBrowserUtil;
import com.happydroids.platform.AndroidDialogOpener;
import com.happydroids.platform.AndroidUncaughtExceptionHandler;
import com.happydroids.platform.Platform;
import com.happydroids.platform.PlatformConnectionMonitor;

public class DroidTowersGooglePlay extends AndroidApplication implements
		BillingController.IConfiguration {
	private static final String TAG = DroidTowersGooglePlay.class.getSimpleName();
	
	private AbstractBillingObserver mBillingObserver;

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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BillingController.unregisterObserver(mBillingObserver); // Avoid
		// receiving
		// notifications after
		// destroy
		BillingController.setConfiguration(null);
	}

	private void onRequestPurchaseResponse(String itemId,
			BillingRequest.ResponseCode response) {

	}

	public BillingController.BillingStatus checkBillingSupported() {
		return BillingController.checkBillingSupported(this);
	}

	public void requestPurchase(String itemId) {
		BillingController.requestPurchase(this, itemId);
	}

	/**
	 * Requests to restore all transactions.
	 */
	public void restoreTransactions() {
		if (!mBillingObserver.isTransactionsRestored()) {
			BillingController.restoreTransactions(this);
		}
	}

	@Override
	public byte[] getObfuscationSalt() {
		return HappyDroidConsts.OBFUSCATION_SALT;
	}

	@Override
	public String getPublicKey() {
		return HappyDroidConsts.OBFUSCATION_KEY;
	}
}

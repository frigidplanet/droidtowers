/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers.scenes;

import static com.happydroids.droidtowers.TowerAssetManager.preloadFinished;
import static com.happydroids.droidtowers.TowerAssetManager.textureAtlas;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.happydroids.HappyDroidConsts;
import com.happydroids.droidtowers.TowerConsts;
import com.happydroids.droidtowers.gamestate.server.TowerGameService;
import com.happydroids.droidtowers.gui.FontManager;
import com.happydroids.droidtowers.gui.VibrateClickListener;
import com.happydroids.droidtowers.gui.WidgetAccessor;
import com.happydroids.droidtowers.platform.Display;
import com.happydroids.droidtowers.scenes.components.MainMenuButtonPanel;
import com.happydroids.droidtowers.tween.TweenSystem;
import com.happydroids.platform.Platform;
import com.happydroids.security.SecurePreferences;

public class MainMenuScene extends SplashScene {
	private static final String TAG = MainMenuScene.class.getSimpleName();
	public static final int BUTTON_WIDTH = Display.devicePixel(280);
	public static final int BUTTON_SPACING = Display.devicePixel(16);

	private boolean builtOutMenu;

	@Override
	public void create(Object... args) {
		super.create(args);

		Label versionLabel = FontManager.Default.makeLabel(String.format("v%s (%s, %s)", HappyDroidConsts.VERSION, HappyDroidConsts.GIT_SHA.substring(0, 8),
				TowerGameService.getDeviceOSMarketName()));
		versionLabel.setColor(Color.LIGHT_GRAY);
		versionLabel.setX(getStage().getWidth() - versionLabel.getWidth() - 5);
		versionLabel.setY(getStage().getHeight() - versionLabel.getHeight() - 5);
		addActor(versionLabel);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		if (!builtOutMenu && preloadFinished()) {
			builtOutMenu = true;

			buildMenuComponents(textureAtlas("hud/menus.txt"));

			SecurePreferences preferences = TowerGameService.instance().getPreferences();

			if (Display.isInCompatibilityMode()) {
				if (!preferences.getBoolean("ANDROID_WARNED_ABOUT_DISPLAY_MODE", false)) {
					preferences.putBoolean("ANDROID_WARNED_ABOUT_DISPLAY_MODE", true);
					Platform.getDialogOpener().showAlert(
							"Compatibility Mode",
							"Hello,\n\nWe're sorry but this game is designed for a device with a higher resolution screen.  "
									+ "It is impossible for us to restrict these devices from the market, so we have "
									+ "designed a compatability mode as a work around.\n\nYou can continue to play the game, "
									+ "but please understand your experience will be degraded.  Most commonly the text will be rather difficult to read.\n\n"
									+ "Thank you,\nDroid Towers Team");
				}
			}
			// new GridObjectDesigner(getStage()).show();
		}
	}

	private void buildMenuComponents(final TextureAtlas menuButtonAtlas) {
		if (progressPanel != null) {
			progressPanel.remove();
		}

		addActor(makeLibGDXLogo(menuButtonAtlas));
		addActor(makeHappyDroidsLogo(menuButtonAtlas));

		MainMenuButtonPanel menuButtonPanel = new MainMenuButtonPanel();
		menuButtonPanel.pack();
		menuButtonPanel.setY(droidTowersLogo.getY() - menuButtonPanel.getHeight());
		menuButtonPanel.setX(-droidTowersLogo.getImageWidth());
		addActor(menuButtonPanel);

		Tween.to(menuButtonPanel, WidgetAccessor.POSITION, CAMERA_PAN_DOWN_DURATION)
				.target(50 + (45 * (droidTowersLogo.getImageWidth() / droidTowersLogo.getWidth())), menuButtonPanel.getY()).ease(TweenEquations.easeInOutExpo)
				.start(TweenSystem.manager());
	}

	@Override
	public void dispose() {
	}

	private Image makeHappyDroidsLogo(TextureAtlas atlas) {
		Image happyDroidsLogo = new Image(atlas.findRegion("happy-droids-logo"));
		happyDroidsLogo.getColor().a = 0f;
		happyDroidsLogo.addAction(Actions.fadeIn(0.125f));
		happyDroidsLogo.setX(getStage().getWidth() - happyDroidsLogo.getWidth() - Display.devicePixel(5));
		happyDroidsLogo.setY(Display.devicePixel(5));
		happyDroidsLogo.addListener(new VibrateClickListener() {
			@Override
			public void onClick(InputEvent event, float x, float y) {
				Platform.getBrowserUtil().launchWebBrowser(TowerConsts.HAPPYDROIDS_URI);
			}
		});
		return happyDroidsLogo;
	}

	private Image makeLibGDXLogo(TextureAtlas atlas) {
		Image libGdxLogo = new Image(atlas.findRegion("powered-by-libgdx"));
		libGdxLogo.getColor().a = 0f;
		libGdxLogo.addAction(Actions.fadeIn(0.125f));
		libGdxLogo.setY(Display.devicePixel(5));
		libGdxLogo.setX(Display.devicePixel(5));
		libGdxLogo.addListener(new VibrateClickListener() {
			@Override
			public void onClick(InputEvent event, float x, float y) {
				Platform.getBrowserUtil().launchWebBrowser("http://libgdx.badlogicgames.com");
			}
		});
		return libGdxLogo;
	}
}

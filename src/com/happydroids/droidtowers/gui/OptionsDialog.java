/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.happydroids.droidtowers.DroidTowersGame;
import com.happydroids.droidtowers.TowerAssetManager;
import com.happydroids.droidtowers.gamestate.server.TowerGameService;
import com.happydroids.droidtowers.platform.Display;
import com.happydroids.security.SecurePreferences;

public class OptionsDialog extends Dialog {
	private final SecurePreferences preferences;
	private final CheckBox fullscreenCheckbox;

	public OptionsDialog(Stage stage) {
		super(stage);

		preferences = TowerGameService.instance().getPreferences();
		fullscreenCheckbox = FontManager.RobotoBold18.makeCheckBox("Fullscreen");
		fullscreenCheckbox.setChecked(Gdx.graphics.isFullscreen());

		setTitle("Options");

		Table c = new Table();
		c.defaults().top().left().space(Display.devicePixel(16));

		c.row().fillX();
		c.add(FontManager.Default.makeLabel("Music Volume"));
		c.add(makeMusicVolumeSlider());

		c.row().fillX();
		c.add(FontManager.Default.makeLabel("Effects Volume"));
		c.add(makeSoundEffectsVolumeSlider());

		c.row().fillX();
		c.add();
		c.add(makeHapticFeedbackCheckbox());

		setView(c);
	}

	private CheckBox makeHapticFeedbackCheckbox() {
		final CheckBox checkBox = FontManager.Roboto18.makeCheckBox("Vibrate on touch");
		checkBox.setChecked(VibrateClickListener.isVibrateEnabled());
		checkBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				VibrateClickListener.setVibrateEnabled(checkBox.isChecked());
				preferences.putBoolean("vibrateOnTouch", checkBox.isChecked());
				preferences.flush();
			}
		});
		return checkBox;
	}

	private Slider makeMusicVolumeSlider() {
		final Slider slider = new Slider(0f, 1f, 0.1f, false, TowerAssetManager.getCustomSkin());
		slider.setValue(DroidTowersGame.getSoundController().getMusicVolume());
		slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				DroidTowersGame.getSoundController().setMusicVolume(slider.getValue());
			}
		});
		return slider;
	}

	private Slider makeSoundEffectsVolumeSlider() {
		final Slider slider = new Slider(0f, 1f, 0.1f, false, TowerAssetManager.getCustomSkin());
		slider.setValue(DroidTowersGame.getSoundController().getEffectsVolume());
		slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				preferences.putFloat("effectsVolume", slider.getValue());
				DroidTowersGame.getSoundController().setEffectsVolume(slider.getValue());
			}
		});
		return slider;
	}
}

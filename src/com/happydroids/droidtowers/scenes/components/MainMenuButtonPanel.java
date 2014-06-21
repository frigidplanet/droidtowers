/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers.scenes.components;

import static com.badlogic.gdx.Application.ApplicationType.Applet;
import static com.happydroids.droidtowers.gui.FontManager.Default;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.happydroids.droidtowers.TowerAssetManager;
import com.happydroids.droidtowers.gui.AboutWindow;
import com.happydroids.droidtowers.gui.LoadTowerWindow;
import com.happydroids.droidtowers.gui.NewTowerDialog;
import com.happydroids.droidtowers.gui.OptionsDialog;
import com.happydroids.droidtowers.gui.VibrateClickListener;
import com.happydroids.droidtowers.platform.Display;

public class MainMenuButtonPanel extends Table {
	private static final String TAG = MainMenuButtonPanel.class.getSimpleName();
	public static final int BUTTON_WIDTH = Display.devicePixel(280);
	public static final int BUTTON_SPACING = Display.devicePixel(12);
	private NinePatch dropShadowPatch;

	public MainMenuButtonPanel() {
		super();

		dropShadowPatch = TowerAssetManager.ninePatch("swatches/drop-shadow.png", Color.WHITE, 22, 22, 22, 22);
		setBackground(TowerAssetManager.ninePatchDrawable(TowerAssetManager.WHITE_SWATCH, Color.DARK_GRAY));

		pad(BUTTON_SPACING);

		if (!Gdx.app.getType().equals(Applet)) {
			padBottom(0);
		}

		defaults().space(BUTTON_SPACING);

		row();
		TextButton loadGameButton = Default.makeTextButton("load tower");
		add(loadGameButton).fill().width(BUTTON_WIDTH);

		row();
		TextButton newGameButton = Default.makeTextButton("new tower");
		add(newGameButton).fill().width(BUTTON_WIDTH);

		row();
		Table optionsAndCreditsRow = new Table();
		optionsAndCreditsRow.row().fill().space(BUTTON_SPACING);
		add(optionsAndCreditsRow).width(BUTTON_WIDTH);

		TextButton optionsButton = Default.makeTextButton("options");
		optionsAndCreditsRow.add(optionsButton).expandX();

		TextButton aboutButton = Default.makeTextButton("credits");
		optionsAndCreditsRow.add(aboutButton).expandX();

		row();
		row().padTop(BUTTON_SPACING);
		TextButton exitGameButton = Default.makeTextButton("exit");
		add(exitGameButton).fill().width(BUTTON_WIDTH);

		exitGameButton.addListener(new VibrateClickListener() {
			@Override
			public void onClick(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		optionsButton.addListener(new VibrateClickListener() {
			@Override
			public void onClick(InputEvent event, float x, float y) {
				new OptionsDialog(getStage()).show();
			}
		});

		newGameButton.addListener(new VibrateClickListener() {
			@Override
			public void onClick(InputEvent event, float x, float y) {
				new NewTowerDialog(getStage()).show();
			}
		});
		loadGameButton.addListener(new VibrateClickListener() {
			@Override
			public void onClick(InputEvent event, float x, float y) {
				new LoadTowerWindow(getStage()).show();
			}
		});

		aboutButton.addListener(new VibrateClickListener() {
			@Override
			public void onClick(InputEvent event, float x, float y) {
				new AboutWindow(getStage()).show();
			}
		});
	}

	@Override
	protected void drawBackground(SpriteBatch batch, float parentAlpha) {
		if (this.dropShadowPatch != null) {
			batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
			this.dropShadowPatch.draw(batch, getX() - dropShadowPatch.getLeftWidth(), getY() - dropShadowPatch.getTopHeight(),
					getWidth() + dropShadowPatch.getRightWidth() + dropShadowPatch.getLeftWidth(), getHeight() + dropShadowPatch.getBottomHeight()
							+ dropShadowPatch.getTopHeight());
		}

		super.drawBackground(batch, parentAlpha);
	}
}

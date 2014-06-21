/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.happydroids.droidtowers.grid.GameGrid;
import com.happydroids.droidtowers.gui.GridObjectPopOver;
import com.happydroids.droidtowers.math.GridPoint;
import com.happydroids.droidtowers.types.RoomType;

public class Lobby extends Room {

	private static final GridPoint TWO_WIDE = new GridPoint(2, 1);
	private final Sprite leftCap;
	private final Sprite rightCap;

	public Lobby(RoomType roomType, GameGrid gameGrid) {
		super(roomType, gameGrid);

		TextureAtlas atlas = roomType.getTextureAtlas();

		leftCap = new Sprite(atlas.findRegion("lobby-left"));
		leftCap.setOrigin(0, 0);
		leftCap.setScale(getGridScale(), getGridScale());

		rightCap = new Sprite(atlas.findRegion("lobby-right"));
		rightCap.setOrigin(0, 0);
		rightCap.setScale(getGridScale(), getGridScale());
	}

	@Override
	public GridObjectPopOver makePopOver() {
		return new GridObjectPopOver(this);
	}

	@Override
	public boolean needsDroids() {
		return false;
	}

	@Override
	public boolean canEarnMoney() {
		return false;
	}

	@Override
	public int getCoinsEarned() {
		return 0;
	}

	@Override
	public int getNumResidents() {
		return 0;
	}

	@Override
	public float getResidencyLevel() {
		return 0f;
	}

	@Override
	public float getDesirability() {
		return 1f;
	}

	@Override
	public float getNoiseLevel() {
		return 0f;
	}

	@Override
	public void render(SpriteBatch spriteBatch, SpriteCache spriteCache, Color renderTintColor) {
		super.render(spriteBatch, spriteCache, renderTintColor);

		// drawEndCaps(spriteBatch);
	}

}

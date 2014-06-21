/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.happydroids.droidtowers.input.InputSystem;
import com.happydroids.droidtowers.math.GridPoint;

public class TowerConsts {
	
	public static final String HAPPYDROIDS_SERVER = "local.happydroids.com";
	public static final String HAPPYDROIDS_URI = "http://" + HAPPYDROIDS_SERVER;
	public static final String HAPPYDROIDS_API_KEY = "f88c02844c913c20f84ea29cbabec97c10c828a8caf204de41";
	public static final boolean DEBUG = true;
	public static boolean DISPLAY_DEBUG_INFO = false;
	public static final long HAPPYDROIDS_PING_FREQUENCY = 180000;
	public static final String VERSION = "1.1.26";
	public static final int VERSION_CODE = 1126;
	public static final String GIT_SHA = "d0381b01a95bcec455df4157bff8a13fc1009724";

	public static final int ONE_DAY = 86400;
	
	public static final float ONE_MEGABYTE = 1048576.0f;

	public static final int GRID_UNIT_SIZE = 64;
	public static final float HALF_GRID_UNIT_SIZE = GRID_UNIT_SIZE / 2;
	public static final int GAME_GRID_START_SIZE = 40;
	public static final int GAME_GRID_EXPAND_LAND_SIZE = 5;

	public static final int LOBBY_FLOOR = 10;
	public static final int GROUND_HEIGHT = GRID_UNIT_SIZE * LOBBY_FLOOR;

	public static final int NEIGHBOR_GROUND_HEIGHT = GRID_UNIT_SIZE * (LOBBY_FLOOR - 3);

	public static final String CURRENCY_SYMBOL = "$";
	public static final float HUD_UPDATE_FREQUENCY = 0.5f;
	public static final float ROOM_UPDATE_FREQUENCY = 10f;
	public static final float JOB_UPDATE_FREQUENCY = ROOM_UPDATE_FREQUENCY + 0.1f;
	public static final float PLAYER_EARNOUT_FREQUENCY = 10.f;
	public static final float TRANSPORT_CALCULATOR_FREQUENCY = 1f;
	public static final float CRIME_CALCULATOR_FREQUENCY = 5f;
	public static final float WEATHER_SERVICE_STATE_CHANGE_FREQUENCY = 60f;
	public static final int WEATHER_SERVICE_STATE_CHANGE_DURATION = 5000;
	public static final float GAME_SAVE_FREQUENCY = 15f;
	public static final long FACEBOOK_CONNECT_DELAY_BETWEEN_TOKEN_CHECK = 3500;
	public static final float MINI_MAP_REDRAW_FREQUENCY = 1f;
	public static final String GAME_SAVE_DIRECTORY = Gdx.app.getType() == Application.ApplicationType.Android ? "towergame/" : ".towergame/";
	public static final String FANDANGO_COMMISSION_JUNCTION_ID = "5737037";
	public static final float ACHIEVEMENT_ENGINE_FREQUENCY = 5f;
	public static final float AVATAR_SPAWN_DELAY = 5f;
	public static final int MAX_AVATARS = (Gdx.app.getType() == Application.ApplicationType.Android ? 20 : 120);
	public static final float AVATAR_POPULATION_SCALE = 0.25f;
	public static final GridPoint SINGLE_POINT = new GridPoint(1, 1);
	public static final int[] NEGATIVE_BUTTON_KEYS = new int[] { InputSystem.Keys.BACK, InputSystem.Keys.ESCAPE };
	public static final int LIMITED_VERSION_MAX_FLOOR = LOBBY_FLOOR + 15;
	public static final float GAME_SPEED_MAX = DEBUG ? 12f : 4f;
	public static final float GAME_SPEED_MIN = DEBUG ? 0.05f : 0.5f;

	public static final boolean ENABLE_AVATAR_LIST_WINDOW = false;
	public static final boolean ENABLE_HAPPYDROIDS_CONNECT = true;
	public static final boolean ENABLE_NEWS_TICKER = false;
	public static final Array<Color> COLORS = new Array<Color>(new Color[] { Color.RED, Color.GREEN, Color.PINK, Color.WHITE, Color.BLUE, Color.LIGHT_GRAY,
			Color.GRAY, Color.DARK_GRAY, Color.ORANGE, Color.YELLOW, Color.MAGENTA, Color.CYAN });
}

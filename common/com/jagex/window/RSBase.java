package com.jagex.window;

public abstract class RSBase {

	public enum ScreenMode {
		FIXED,
		RESIZABLE,
		FULLSCREEN;
	}

	public static int portOff;
	public static ScreenMode frameMode = ScreenMode.FIXED;

	public abstract void init();
	
	public abstract void startUp();

	public abstract void processGameLoop();

	public abstract void processDrawing();

	public abstract void cleanUpForQuit();

	public abstract void raiseWelcomeScreen();

	
	public abstract void writepath(int currentIndex, int movementType, int[] waypointX, int[] waypointY);
}

package de.entwicklerpages.java.schoolgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.entwicklerpages.java.schoolgame.SchoolGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "School Game";
		config.useGL30 = false;
		config.forceExit = true;
		new LwjglApplication(new SchoolGame(), config);
	}
}

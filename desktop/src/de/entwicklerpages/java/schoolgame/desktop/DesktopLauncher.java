package de.entwicklerpages.java.schoolgame.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import de.entwicklerpages.java.schoolgame.SchoolGame;

public class DesktopLauncher {
    public static void main (String[] arg) {

        String basePath = LogStream.getBasePath("schoolgame");

        try
        {
            // Leitet die Logs in eine Datei um.

            String logPath = basePath;

            if (!LogStream.pathIsAbsolute())
            {
                logPath = System.getProperty("user.home") + "/" + basePath;
            }

            FileOutputStream fout = new FileOutputStream(logPath + "application.log");
            FileOutputStream ferr = new FileOutputStream(logPath + "error.log");

            LogStream multiOut = new LogStream(System.out, fout);
            LogStream multiErr = new LogStream(System.err, ferr);

            PrintStream stdout = new PrintStream(multiOut);
            PrintStream stderr = new PrintStream(multiErr);

            System.setOut(stdout);
            System.setErr(stderr);
        }
        catch (FileNotFoundException ex)
        {
            // Nicht schlimm, dann werden die Logs nur in der Konsole angezeigt.
            System.err.println("ERROR: Cannot write to log files. Logs are not saved!");
        }

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "School Game";
        config.useGL30 = false;
        config.forceExit = true;
        config.resizable = true;
        config.width = Math.round(1280 / 1.4f);
        config.height = Math.round(720 / 1.4f);
        config.foregroundFPS = 60;
        config.backgroundFPS = 30; // Im Hintergrund braucht ds Spiel nicht so viele FPS
        config.preferencesDirectory = basePath;
        config.preferencesFileType = LogStream.pathIsAbsolute() ? Files.FileType.Absolute : Files.FileType.External;
        new LwjglApplication(new SchoolGame(), config);
    }
}

package de.entwicklerpages.java.schoolgame.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import de.entwicklerpages.java.schoolgame.SchoolGame;

/**
 * Desktop Starter für das Spiel. Basiert auf dem libgdx Template.
 *
 * @author nico
 */
public class DesktopLauncher {

    /**
     * Einstiegspunkt für das Program. Initialisiert das Spiel und gibt die Kontrolle weiter
     * an eine {@link SchoolGame} Instanz.
     *
     * @param arg der Argumentvektor. Wird nicht benutzt.
     *
     * @see SchoolGame
     */
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

            // Verzeichnis anlegen wenn es nicht existiert
            new File(logPath).mkdirs();

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
        config.useHDPI = false;
        config.width = Math.round(1280 / 1.4f);
        config.height = Math.round(720 / 1.4f);
        config.foregroundFPS = 60;
        config.backgroundFPS = 30; // Im Hintergrund braucht ds Spiel nicht so viele FPS
        config.preferencesDirectory = basePath;
        config.preferencesFileType = LogStream.pathIsAbsolute() ? Files.FileType.Absolute : Files.FileType.External;
        new LwjglApplication(new SchoolGame(), config);
    }
}

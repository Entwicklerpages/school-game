package de.entwicklerpages.java.schoolgame.desktop;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Fasst zwei OutputStreams zusammen.
 * Wird benutzt um Logs sowohl in der Konsole auszugeben, als auch in einer Datei zu speichern.
 *
 * @author nico
 */
public class LogStream extends OutputStream {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Speichert, ob der erfasste Log Pfad absolut oder relativ zum Home-Verzeichnis ist.
     *
     * @see LogStream#getBasePath(String)
     * @see LogStream#pathIsAbsolute()
     */
    private static boolean absolutePath = false;

    /**
     * Der OutputStream, der die Logs in die Konsole leitet.
     */
    private OutputStream consoleStream;

    /**
     * Der OutputStream, der die Logs in eine Datei leitet.
     */
    private OutputStream logStream;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Standardkonstruktor, legt die Streams fest.
     *
     * @param consoleStream Der Konsolen Stream
     * @param logStream Der Datei Stream
     */
    public LogStream(OutputStream consoleStream, OutputStream logStream)
    {
        this.consoleStream = consoleStream;
        this.logStream = logStream;
    }

    @Override
    public void write(int b) throws IOException
    {
        consoleStream.write(b);
        logStream.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        consoleStream.write(b);
        logStream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        consoleStream.write(b, off, len);
        logStream.write(b, off, len);
    }

    @Override
    public void flush() throws IOException
    {
        consoleStream.flush();
        logStream.flush();
    }

    @Override
    public void close() throws IOException
    {
        consoleStream.close();
        logStream.close();
    }

    /**
     * Berechnet den Pfad für Logs und Spieldaten.
     *
     * @param game Name des Spielverzeichnisses
     * @return der betriebssystemspeziefische Pfad
     */
    public static String getBasePath(String game)
    {
        String os = System.getProperty("os.name").toLowerCase();

        String base = ".prefs/" + game + "/";

        if (os.contains("win"))
        {
            String appdata = System.getenv("APPDATA");

            if (appdata != null)
            {
                base = appdata + File.separator + game + File.separator;
                absolutePath = true;
            }
        }
        else if (os.contains("mac"))
        {
            base = "Library/Application Support/" + game + "/";
        }

        return base;
    }

    /**
     * Gibt zurück, ob der berechnete Pfad absolut oder relativ ist.
     *
     * WICHTIG: Erst muss {@link #getBasePath(String)} aufgerufen werden!
     *
     * @return true wenn der Pfad absolut ist, sonst false
     *
     * @see LogStream#absolutePath
     * @see LogStream#getBasePath(String)
     */
    public static boolean pathIsAbsolute()
    {
        return absolutePath;
    }
}

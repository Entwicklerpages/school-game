package de.entwicklerpages.java.schoolgame.desktop;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class LogStream extends OutputStream {
    OutputStream consoleStream;
    OutputStream logStream;

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

    private static boolean absolutePath = false;

    // TODO: Auf Windows und Linux testen!
    public static String getBasePath(String game)
    {
        String os = System.getProperty("os.name").toLowerCase();

        String base = ".prefs/" + game + "/";

        if (os.contains("win"))
        {
            String appdata = System.getenv("APPDATA");

            if (appdata != null)
            {
                base = appdata + File.pathSeparator + game + File.pathSeparator;
                absolutePath = true;
            }
        }
        else if (os.contains("mac"))
        {
            base = "Library/Application Support/" + game + "/";
        }

        return base;
    }

    public static boolean pathIsAbsolute()
    {
        return absolutePath;
    }
}

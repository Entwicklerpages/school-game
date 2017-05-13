package de.entwicklerpages.java.schoolgame.tools;


import java.io.File;

/**
 * Helferklasse für Pfade.
 *
 * @author nico
 */
public final class PathHelper {

    private final static String GAME_NAME = "legende_kristalle";

    private PathHelper() {}

    /**
     * Gibt den Basispfad für Speicherdateien zurück.
     *
     * @return der Pfad
     */
    public static String getBasePath()
    {
        String os = System.getProperty("os.name").toLowerCase();

        String base = ".prefs/" + GAME_NAME + "/";

        boolean absolutePath = false;

        if (os.contains("win"))
        {
            String appdata = System.getenv("APPDATA");

            if (appdata != null)
            {
                base = appdata + File.separator + GAME_NAME + File.separator;
                absolutePath = true;
            }
        }
        else if (os.contains("mac"))
        {
            base = "Library/Application Support/" + GAME_NAME + "/";
        }

        if (!absolutePath)
        {
            base = System.getProperty("user.home") + File.separator + base;
        }

        return base;
    }

    /**
     * Gibt den Pfad zum assets/data Verzeichnis im Projekt zurück.
     * Wird der Pfad nicht gefunden, dann wird das Arbeitsverzeichnis zurückgegeben.
     *
     * @return der Pfad
     */
    public static File getAssetDirIfFound()
    {
        File workingDir = new File(System.getProperty("user.dir"));

        File assetDir = new File(workingDir.getAbsolutePath() + File.separator + "core" + File.separator + "assets" + File.separator + "data");

        if (assetDir.exists() && assetDir.isDirectory())
        {
            return assetDir;
        }

        return workingDir;
    }

    /**
     * Gibt den Pfad zum assets/data/dialog Verzeichnis im Projekt zurück.
     * Wird der Pfad nicht gefunden, dann wird das assets/data Verzeichnis oder das Arbeitsverzeichnis zurückgegeben.
     *
     * @return der Pfad
     */
    public static File getDialogDirIfFound()
    {
        File dialogAssetDir = new File(getAssetDirIfFound().getAbsolutePath() + File.separator + "dialog");

        if (dialogAssetDir.exists() && dialogAssetDir.isDirectory())
        {
            return dialogAssetDir;
        }

        return getAssetDirIfFound();
    }

    /**
     * Gibt den Pfad zum assets/data/map Verzeichnis im Projekt zurück.
     * Wird der Pfad nicht gefunden, dann wird das assets/data Verzeichnis oder das Arbeitsverzeichnis zurückgegeben.
     *
     * @return der Pfad
     */
    public static File getMapDirIfFound()
    {
        File mapAssetDir = new File(getAssetDirIfFound().getAbsolutePath() + File.separator + "maps");

        if (mapAssetDir.exists() && mapAssetDir.isDirectory())
        {
            return mapAssetDir;
        }

        return getAssetDirIfFound();
    }

    /**
     * Gibt den Build Pfad zurück.
     *
     * @return der Pfad
     */
    public static File getBuildDirIfFound()
    {
        File workingDir = new File(System.getProperty("user.dir"));

        File assetDir = new File(workingDir.getAbsolutePath() + File.separator + "desktop" + File.separator + "build" + File.separator + "libs");

        if (assetDir.exists() && assetDir.isDirectory())
        {
            return assetDir;
        }

        return workingDir;
    }

    /**
     * Gibt den Pfad zum packr Subverzeichnis des Tools zurück.
     *
     * @return der Pfad
     */
    public static File getPackrDirIfFound()
    {
        File workingDir = new File(System.getProperty("user.dir"));

        File assetDir = new File(workingDir.getAbsolutePath() + File.separator + "tools" + File.separator + "packr");

        if (assetDir.exists() && assetDir.isDirectory())
        {
            return assetDir;
        }

        return workingDir;
    }
}

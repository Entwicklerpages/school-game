package de.entwicklerpages.java.schoolgame.tools;


import java.io.File;

public final class PathHelper {

    private final static String GAME_NAME = "schoolgame";

    private PathHelper() {}

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

    public static File getDialogDirIfFound()
    {
        File dialogAssetDir = new File(getAssetDirIfFound().getAbsolutePath() + File.separator + "dialog");

        if (dialogAssetDir.exists() && dialogAssetDir.isDirectory())
        {
            return dialogAssetDir;
        }

        return getAssetDirIfFound();
    }

    public static File getMapDirIfFound()
    {
        File mapAssetDir = new File(getAssetDirIfFound().getAbsolutePath() + File.separator + "maps");

        if (mapAssetDir.exists() && mapAssetDir.isDirectory())
        {
            return mapAssetDir;
        }

        return getAssetDirIfFound();
    }
}

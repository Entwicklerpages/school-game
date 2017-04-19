package de.entwicklerpages.java.schoolgame.tools.pack;

import com.badlogicgames.packr.Packr;
import com.badlogicgames.packr.PackrConfig;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class PackUtility
{
    private PackrConfig config;
    private String outDir;

    public PackUtility()
    {
        config = new PackrConfig();
        config.vmArgs = Collections.emptyList();
    }

    public void setOutDir(String outDir)
    {
        outDir = outDir.trim();
        if (outDir.endsWith(File.separator))
            this.outDir = outDir;
        else
            this.outDir = outDir + File.separator;
    }

    public void setExecutable(String executable)
    {
        config.executable = executable.trim();
    }

    public void setJDK(String jdk)
    {
        config.jdk = jdk.trim();
    }

    public void setMainClass(String mainClass)
    {
        config.mainClass = mainClass.trim();
    }

    public void setBundleIdentifier(String bundleIdentifier)
    {
        config.bundleIdentifier = bundleIdentifier.trim();
    }

    public void setSourceJar(String sourceJar)
    {
        config.classpath = Collections.singletonList(sourceJar.trim());
    }

    public void setMinimizeJre(String minimizeJre)
    {
        config.minimizeJre = minimizeJre.trim();
    }

    public void setPlatform(PackrConfig.Platform platform)
    {
        config.platform = platform;
    }

    public void pack() throws IOException
    {
        switch (config.platform)
        {
            case Windows32:
                config.outDir = new File(outDir + "win32/");
                break;

            case Windows64:
                config.outDir = new File(outDir + "win64/");
                break;

            case Linux32:
                config.outDir = new File(outDir + "linux32/");
                break;

            case Linux64:
                config.outDir = new File(outDir + "linux64/");
                break;

            case MacOS:
                config.outDir = new File(outDir + "mac/" + config.executable + ".app/");
                break;
        }

        new Packr().pack(config);
    }
}

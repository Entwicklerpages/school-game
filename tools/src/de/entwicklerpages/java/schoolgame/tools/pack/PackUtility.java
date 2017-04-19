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

    public void packWin32() throws IOException
    {
        config.platform = PackrConfig.Platform.Windows32;
        config.outDir = new File(outDir + "win32/");

        new Packr().pack(config);
    }

    public void packWin64() throws IOException
    {
        config.platform = PackrConfig.Platform.Windows64;
        config.outDir = new File(outDir + "win64/");

        new Packr().pack(config);
    }
    public void packMacOS() throws IOException
    {
        config.platform = PackrConfig.Platform.MacOS;
        config.outDir = new File(outDir + "mac/" + config.executable + ".app/");

        new Packr().pack(config);
    }

    public void packLinux32() throws IOException
    {
        config.platform = PackrConfig.Platform.Linux32;
        config.outDir = new File(outDir + "linux32/");

        new Packr().pack(config);
    }

    public void packLinux64() throws IOException
    {
        config.platform = PackrConfig.Platform.Linux64;
        config.outDir = new File(outDir + "linux64/");

        new Packr().pack(config);
    }
}

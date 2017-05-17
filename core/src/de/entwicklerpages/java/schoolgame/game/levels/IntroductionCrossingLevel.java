package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Einstiegs Level
 *
 * LEVEL INFO
 * Hauptkreuzung: Weg zur Höhle
 *
 * @author nico
 */
public class IntroductionCrossingLevel extends Level
{
    public static final String LEVEL_NAME = "introduction_crossing";

    public IntroductionCrossingLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Einführung: Kreuzung";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

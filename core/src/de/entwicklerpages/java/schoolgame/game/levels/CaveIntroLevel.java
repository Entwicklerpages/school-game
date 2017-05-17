package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Erstes Höhlen Level
 *
 * LEVEL INFO
 * Zum ersten mal in der Höhle.
 *
 * @author nico
 */
public class CaveIntroLevel extends Level
{
    public static final String LEVEL_NAME = "cave_intro";

    public CaveIntroLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Höhle: Einführung";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

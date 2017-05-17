package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Viertes Höhlen Level
 *
 * LEVEL INFO
 * Bosskampf
 *
 * @author nico
 */
public class CaveBossLevel extends Level
{
    public static final String LEVEL_NAME = "cave_boss";

    public CaveBossLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Höhle: Bosskampf";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

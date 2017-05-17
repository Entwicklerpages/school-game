package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Höhlen Level
 *
 * LEVEL INFO
 * Hauptkreuzung: Weg zum Wald
 *
 * @author nico
 */
public class CaveCrossingLevel extends Level
{
    public static final String LEVEL_NAME = "cave_crossing";

    public CaveCrossingLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Höhle: Kreuzung";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

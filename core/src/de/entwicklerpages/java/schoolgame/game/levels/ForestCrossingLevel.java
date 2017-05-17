package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Nach dem Wald zurück im Dorf
 *
 * LEVEL INFO
 * Neue Aufgabe.
 *
 * @author nico
 */
public class ForestCrossingLevel extends Level
{
    public static final String LEVEL_NAME = "forest_crossing";

    public ForestCrossingLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Wald: Kreuzung";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

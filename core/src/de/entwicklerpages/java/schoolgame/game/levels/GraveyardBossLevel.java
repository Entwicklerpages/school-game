package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Fünftes Friedhof Level
 *
 * LEVEL INFO
 * -----
 * Story
 *
 * @author nico
 */
public class GraveyardBossLevel extends Level
{
    public static final String LEVEL_NAME = "graveyard_boss";

    public GraveyardBossLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Friedhof: Bosskampf";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

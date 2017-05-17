package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Fünftes Friedhof Level
 *
 * LEVEL INFO
 * -----
 * Story - Endkampf Drache
 *
 * @author nico
 */
public class GraveyardBossDragonLevel extends Level
{
    public static final String LEVEL_NAME = "graveyard_boss_dragon";

    public GraveyardBossDragonLevel() {
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

package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Fünftes Friedhof Level
 *
 * LEVEL INFO
 * -----
 * Story - Endkampf Wache
 *
 * @author nico
 */
public class GraveyardBossWarriorLevel extends Level
{
    public static final String LEVEL_NAME = "graveyard_boss_warrior";

    public GraveyardBossWarriorLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Friedhof: Bosskampf";
    }

    @Override
    protected String getMusicName()
    {
        return "netherworld_shanty";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

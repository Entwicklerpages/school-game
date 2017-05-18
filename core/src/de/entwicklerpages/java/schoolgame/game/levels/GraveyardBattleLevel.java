package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Zweites Friedhof Level
 *
 * LEVEL INFO
 * -----
 * Keine Story, nur Kampf!
 *
 * @author nico
 */
public class GraveyardBattleLevel extends Level
{
    public static final String LEVEL_NAME = "graveyard_battle";

    public GraveyardBattleLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Friedhof: Kampf";
    }

    @Override
    protected String getMusicName()
    {
        return "myst_on_the_moor";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

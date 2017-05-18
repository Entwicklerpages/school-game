package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Zweites Wald Level
 *
 * LEVEL INFO
 * -----
 * Keine Story, nur Kampf!
 *
 * @author nico
 */
public class ForestBattle extends Level
{
    public static final String LEVEL_NAME = "forest_battle";

    public ForestBattle() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Wald: Kampf";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

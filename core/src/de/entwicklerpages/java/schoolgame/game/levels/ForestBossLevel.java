package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Drittes Wald Level
 *
 * LEVEL INFO
 * Bosskampf.
 *
 * @author nico
 */
public class ForestBossLevel extends Level
{
    public static final String LEVEL_NAME = "forest_boss";

    public ForestBossLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Wald: Bosskampf";
    }

    @Override
    protected String getMusicName()
    {
        return "future_gladiator";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

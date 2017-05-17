package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Ende pro-Wald Level
 *
 * LEVEL INFO
 * -----
 * Story - Krieger ist besiegt.
 *
 * @author nico
 */
public class EndingForestLevel extends Level
{
    public static final String LEVEL_NAME = "ending_forest";

    public EndingForestLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Ende: Wald";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

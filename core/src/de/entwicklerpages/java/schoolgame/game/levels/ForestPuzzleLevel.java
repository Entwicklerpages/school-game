package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Drittes Wald Level
 *
 * LEVEL INFO
 * Rätsellevel
 *
 * @author nico
 */
public class ForestPuzzleLevel extends Level
{
    public static final String LEVEL_NAME = "forest_puzzle";

    public ForestPuzzleLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Wald: Rätsel";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

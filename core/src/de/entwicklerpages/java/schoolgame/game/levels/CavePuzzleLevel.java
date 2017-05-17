package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Drittes Höhlen Level
 *
 * LEVEL INFO
 * Rätsellevel
 *
 * @author nico
 */
public class CavePuzzleLevel extends Level
{
    public static final String LEVEL_NAME = "cave_puzzle";

    public CavePuzzleLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Höhle: Rätsel";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

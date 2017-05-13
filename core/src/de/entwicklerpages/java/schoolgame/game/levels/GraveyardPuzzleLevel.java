package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Drittes Friedhof Level
 *
 * LEVEL INFO
 * -----
 * Rätsellevel
 *
 * @author nico
 */
public class GraveyardPuzzleLevel extends Level
{
    public static final String LEVEL_NAME = "graveyard_puzzle";

    public GraveyardPuzzleLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Friedhof: Rätsel";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

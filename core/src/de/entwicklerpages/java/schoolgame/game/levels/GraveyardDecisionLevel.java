package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Viertes Friedhof Level
 *
 * LEVEL INFO
 * -----
 * Story
 *
 * @author nico
 */
public class GraveyardDecisionLevel extends Level
{
    public static final String LEVEL_NAME = "graveyard_decision";

    public GraveyardDecisionLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Friedhof: Entscheidung";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

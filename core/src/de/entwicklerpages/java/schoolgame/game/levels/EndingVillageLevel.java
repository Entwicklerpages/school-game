package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Ende pro-Dorf Level
 *
 * LEVEL INFO
 * -----
 * Story - Drache ist besiegt.
 *
 * @author nico
 */
public class EndingVillageLevel extends Level
{
    public static final String LEVEL_NAME = "ending_village";

    public EndingVillageLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Ende: Dorf";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

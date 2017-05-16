package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Zweites Höhlen Level
 *
 * LEVEL INFO
 * -----
 * Keine Story, nur Kampf!
 *
 * @author nico
 */
public class CaveBattleLevel extends Level
{
    public static final String LEVEL_NAME = "cave_battle";

    public CaveBattleLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Höhle: Kampf";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

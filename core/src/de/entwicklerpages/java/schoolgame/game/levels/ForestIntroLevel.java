package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Erstes Wald Level
 *
 * LEVEL INFO
 * Zum ersten mal im Wald.
 *
 * @author nico
 */
public class ForestIntroLevel extends Level
{
    public static final String LEVEL_NAME = "forest_intro";

    public ForestIntroLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Wald: Einführung";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

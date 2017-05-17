package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Nach der Höhle im Dorf
 *
 * LEVEL INFO
 * Ergebnisse des Kampfes
 *
 * @author nico
 */
public class CaveVillageLevel extends Level
{
    public static final String LEVEL_NAME = "cave_village";

    public CaveVillageLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Höhle: Dorf";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

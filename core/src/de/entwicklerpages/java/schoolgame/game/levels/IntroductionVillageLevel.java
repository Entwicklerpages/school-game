package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Einstiegs Level
 *
 * LEVEL INFO
 * Erstes mal im Dorf. Bringt dem Spieler die Story näher.
 *
 * @author nico
 */
@SuppressWarnings("unused") // Dise Klasse wird über Reflection geladen
public class IntroductionVillageLevel extends Level
{
    public static final String LEVEL_NAME = "introduction_village";

    public IntroductionVillageLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Einführung: Dorf";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.CutScene;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Einstiegs Level
 *
 * LEVEL INFO
 * Erstes echte Level. Bringt dem Spieler die Story näher.
 *
 * @author nico
 */
@SuppressWarnings("unused") // Dise Klasse wird über Reflection geladen
public class IntruductionLevel extends Level {
    public static final String LEVEL_NAME = "introduction";

    public IntruductionLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Einführung";
    }

    @Override
    protected CutScene getIntroCutScene()
    {
        return new CutScene(null, "intro");
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

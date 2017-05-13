package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Erstes Friedhof Level
 *
 * LEVEL INFO
 * -----
 * Einführung in den Friedhof
 *
 * @author nico
 */
@SuppressWarnings("unused") // Dise Klasse wird über Reflection geladen
public class GraveyardIntroLevel extends Level
{
    public static final String LEVEL_NAME = "graveyard_intro";

    public GraveyardIntroLevel() {
        super("graveyard_intro");
    }

    @Override
    public String getTitle() {
        return "Friedhof - 1";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
    }
}

package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;

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
        super("introduction");
    }

    @Override
    public String getTitle() {
        return "Einführung";
    }
}

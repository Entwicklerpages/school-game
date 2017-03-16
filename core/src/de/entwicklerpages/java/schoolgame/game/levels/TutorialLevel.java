package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;

/**
 * Tutorial Level
 *
 * LEVEL INFO
 * Bietet dem Spieler eine Anleitung zum Spiel.
 * Trägt nicht zur Story bei.
 *
 * @author nico
 */
@SuppressWarnings("unused") // Dise Klasse wird über Reflection geladen
public class TutorialLevel extends Level {
    public static final String LEVEL_NAME = "tutorial";

    public TutorialLevel() {
        super("tutorial");
    }

    @Override
    public String getTitle() {
        return "Tutorial";
    }
}


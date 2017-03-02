package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.game.Level;

@SuppressWarnings("unused") // Dise Klasse wird über Reflection geladen
public class TutorialLevel extends Level {
    public static final String LEVEL_NAME = "tutorial";

    public TutorialLevel() {
        super("test");
    }

    @Override
    public String getTitle() {
        return "Tutorial";
    }
}


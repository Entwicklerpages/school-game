package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class MainMenu extends MenuState {

    @Override
    public String getStateName() {
        return "MAIN_MENU";
    }

    @Override
    String getI18nName() {
        return "MainMenu";
    }

    @Override
    void setupMenu() {
        MenuEntry newGame = new MenuEntry("neues_spiel");
        newGame.setActiveColor(Color.GREEN);

        MenuEntry loadGame = new MenuEntry("spiel_laden");
        loadGame.setActiveColor(Color.GREEN);
        loadGame.setCallback(new MenuCallback() {
            @Override
            public void run() {
                game.setGameState(new LoadGameMenu());
            }
        });

        MenuEntry options = new MenuEntry("optionen");
        options.setActiveColor(Color.GREEN);
        options.setCallback(new MenuCallback() {
            @Override
            public void run() {
                game.setGameState(new OptionsMenu());
            }
        });

        MenuEntry credits = new MenuEntry("credits");
        credits.setActiveColor(Color.GREEN);
        credits.setCallback(new MenuCallback() {
            @Override
            public void run() {
                game.setGameState(new Credits());
            }
        });

        MenuEntry quit = new MenuEntry("beenden");
        quit.setActiveColor(Color.RED);
        quit.setCallback(new MenuCallback() {
            @Override
            public void run() {
                Gdx.app.exit();
            }
        });

        addEntry(newGame);
        addEntry(loadGame);
        addEntry(options);
        addEntry(credits);
        addEntry(quit);
    }
}

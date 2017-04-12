package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;

/**
 * Das Hauptmenü des Spiels.
 *
 * @author nico
 */
public class MainMenu extends MenuState {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String getStateName() {
        return "MAIN_MENU";
    }

    @Override
    String getI18nName() {
        return "MainMenu";
    }

    /**
     * Erstellt die entsprechende Menüstruktur und legt die Callbacks fest.
     *
     * * Neues Spiel
     * * Spiel laden
     * * Optionen
     * * Credits
     * * Beenden
     */
    @Override
    void setupMenu() {

        MenuTitle title = new MenuTitle("titel");
        title.setColor(Color.CORAL);

        MenuEntry newGame = new MenuEntry("neues_spiel");
        newGame.setActiveColor(Color.GREEN);
        newGame.setCallback(new ActionCallback() {
            @Override
            public void run() {
                game.setGameState(new NewGameMenu());
            }
        });

        MenuEntry loadGame = new MenuEntry("spiel_laden");
        loadGame.setActiveColor(Color.GREEN);
        loadGame.setCallback(new ActionCallback() {
            @Override
            public void run() {
                game.setGameState(new LoadGameMenu());
            }
        });

        MenuEntry options = new MenuEntry("optionen");
        options.setActiveColor(Color.GREEN);
        options.setCallback(new ActionCallback() {
            @Override
            public void run() {
                game.setGameState(new OptionsMenu());
            }
        });

        MenuEntry credits = new MenuEntry("credits");
        credits.setActiveColor(Color.GREEN);
        credits.setCallback(new ActionCallback() {
            @Override
            public void run() {
                game.setGameState(new Credits());
            }
        });

        MenuEntry quit = new MenuEntry("beenden");
        quit.setActiveColor(Color.RED);
        quit.setCallback(new ActionCallback() {
            @Override
            public void run() {
                Gdx.app.exit();
            }
        });

        addEntry(new MenuSpacer(40));
        addEntry(title);
        addEntry(new MenuSpacer(70));
        addEntry(newGame);
        addEntry(loadGame);
        addEntry(options);
        addEntry(credits);
        addEntry(quit);
    }
}

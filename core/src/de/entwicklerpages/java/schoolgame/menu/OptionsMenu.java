package de.entwicklerpages.java.schoolgame.menu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.common.CheatDetector;

public class OptionsMenu extends MenuState {

    @Override
    public String getStateName() {
        return "OPTIONS_MENU";
    }

    @Override
    String getI18nName() {
        return "OptionsMenu";
    }

    @Override
    void setupMenu() {
        MenuTitle label = new MenuTitle("titel");
        label.setColor(Color.CORAL);

        MenuEntry back = new MenuEntry("zurueck");
        back.setActiveColor(Color.YELLOW);
        back.setCallback(new ActionCallback() {
            @Override
            public void run() {
                game.setGameState(new MainMenu());
                game.getPreferences().flush();
            }
        });

        final MenuEntry fullscreen = new MenuEntry(Gdx.graphics.isFullscreen() ? "fullscreen_on" : "fullscreen_off");
        fullscreen.setActiveColor(Color.YELLOW);
        fullscreen.setCallback(new ActionCallback() {
            @Override
            public void run() {
                if (Gdx.graphics.isFullscreen())
                {
                    fullscreen.setLabel("fullscreen_off");
                    int width = 1280, height = 720;

                    if (Gdx.graphics.getWidth() < width) // graphics.getWidth() gibt zwar nur die Breite des Fensters, aber da wir noch Fullscreen sind, sollte das die richtige Breite sein.
                    {
                        width /= 2;
                        height /= 2;
                    }

                    game.setFullscreen(false);
                    Gdx.input.setCursorCatched(false);
                    Gdx.graphics.setWindowedMode(width, height);
                } else {
                    fullscreen.setLabel("fullscreen_on");
                    game.setFullscreen(true);
                    Gdx.input.setCursorCatched(true);
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                }
            }
        });

        final MenuEntry vsync = new MenuEntry(game.shouldVSync() ? "vsync_on" : "vsync_off");
        vsync.setActiveColor(Color.YELLOW);
        vsync.setCallback(new ActionCallback() {
            @Override
            public void run() {
                if (game.shouldVSync())
                {
                    vsync.setLabel("vsync_off");
                    game.setVSync(false);
                    Gdx.graphics.setVSync(false);
                } else {
                    vsync.setLabel("vsync_on");
                    game.setVSync(true);
                    Gdx.graphics.setVSync(true);
                }
            }
        });

        MenuEntry delete = new MenuEntry("alles_loeschen");
        delete.setActiveColor(Color.RED);
        delete.setCallback(new ActionCallback() {
            @Override
            public void run() {
                game.setGameState(new DeleteDataMenu());
            }
        });

        CheatDetector cheatDetector = new CheatDetector();

        // Hier wird eine absolut zufällige Sequenz festgelegt, um die Cheats vor normalen Spielern zu verstecken.
        cheatDetector.setKeySequence(new int[] {
                Input.Keys.UP,
                Input.Keys.UP,
                Input.Keys.DOWN,
                Input.Keys.DOWN,
                Input.Keys.LEFT,
                Input.Keys.RIGHT,
                Input.Keys.LEFT,
                Input.Keys.RIGHT,
                Input.Keys.B,
                Input.Keys.A
        });

        cheatDetector.setCallback(new ActionCallback() {
            @Override
            public void run() {
                game.setGameState(new Splashscreen());
                // TODO Zu einem Cheatmenü wechseln
            }
        });

        game.addInputProcessor(cheatDetector);

        addEntry(new MenuSpacer(40));
        addEntry(label);
        addEntry(new MenuSpacer(20));
        addEntry(back);
        addEntry(new MenuSpacer(60));
        addEntry(fullscreen);
        addEntry(vsync);
        addEntry(delete);
    }
}

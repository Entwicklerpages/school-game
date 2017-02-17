package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class LoadGameMenu extends MenuState {

    @Override
    public String getStateName() {
        return "LOAD_GAME_MENU";
    }

    @Override
    String getI18nName() {
        return "LoadGameMenu";
    }

    @Override
    void setupMenu() {
        MenuEntry back = new MenuEntry("zurueck");
        back.setActiveColor(Color.YELLOW);
        back.setCallback(new MenuCallback() {
            @Override
            public void run() {
                game.setGameState(new MainMenu());
            }
        });

        MenuLabel label = new MenuLabel("auswaehlen");
        label.setColor(Color.LIGHT_GRAY);

        MenuEntry loadGame1 = new MenuEntry("slot_1");
        loadGame1.setActiveColor(Color.GREEN);

        MenuEntry loadGame2 = new MenuEntry("slot_2");
        loadGame2.setActiveColor(Color.GREEN);

        MenuEntry loadGame3 = new MenuEntry("slot_3");
        loadGame3.setActiveColor(Color.GREEN);

        MenuEntry loadGame4 = new MenuEntry("slot_4");
        loadGame4.setActiveColor(Color.GREEN);

        MenuEntry loadGame5 = new MenuEntry("slot_5");
        loadGame5.setActiveColor(Color.GREEN);

        addEntry(new MenuSpacer(20));
        addEntry(back);
        addEntry(new MenuSpacer(40));
        addEntry(label);
        addEntry(new MenuSpacer(10));
        addEntry(loadGame1);
        addEntry(loadGame2);
        addEntry(loadGame3);
        addEntry(loadGame4);
        addEntry(loadGame5);
    }
}

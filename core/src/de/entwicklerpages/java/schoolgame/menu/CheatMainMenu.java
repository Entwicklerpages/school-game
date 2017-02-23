package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;


public class CheatMainMenu extends MenuState {
    @Override
    public String getStateName() {
        return "CHEAT_MAIN_MENU";
    }

    @Override
    String getI18nName() {
        return "Cheats";
    }

    @Override
    void setupMenu() {

        MenuTitle title = new MenuTitle("titel");
        title.setColor(Color.PINK);

        MenuEntry modSaveData = new MenuEntry("speicherstand");
        modSaveData.setCallback(new ActionCallback() {
            @Override
            public void run() {
                game.setGameState(new Credits());
            }
        });

        MenuEntry back = new MenuEntry("zurueck");
        back.setCallback(new ActionCallback() {
            @Override
            public void run() {
                game.setGameState(new MainMenu());
            }
        });

        addEntry(new MenuSpacer(40));
        addEntry(title);
        addEntry(new MenuSpacer(70));
        addEntry(modSaveData);
        addEntry(back);
    }
}

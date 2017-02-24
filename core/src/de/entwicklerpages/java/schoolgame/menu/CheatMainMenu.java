package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.CheatManager;


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

        final MenuEntry immortality = new MenuEntry(CheatManager.getInstance().isImmortal() ? "unsterblich_on" : "unsterblich_off");
        immortality.setCallback(new ActionCallback() {
            @Override
            public void run() {
                if (CheatManager.getInstance().isImmortal())
                {
                    immortality.setLabel("unsterblich_off");
                    CheatManager.getInstance().setImmortality(false);
                } else {
                    immortality.setLabel("unsterblich_on");
                    CheatManager.getInstance().setImmortality(true);
                }
            }
        });

        MenuEntry modSaveData = new MenuEntry("speicherstand");
        modSaveData.setCallback(new ActionCallback() {
            @Override
            public void run() {
                game.setGameState(new CheatSlotSelectionMenu());
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
        addEntry(immortality);
        addEntry(modSaveData);
        addEntry(new MenuSpacer(25));
        addEntry(back);
    }
}

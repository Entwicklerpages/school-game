package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.graphics.Color;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.SaveData;


public class CheatSlotSelectionMenu extends LoadGameMenu {
    @Override
    public String getStateName() {
        return "CHEAT_SLOT_SELECTION_MENU";
    }

    @Override
    String getI18nName() {
        return "Cheats";
    }

    @Override
    void setupMenu() {
        MenuEntry back = new MenuEntry("zurueck");
        back.setActiveColor(Color.YELLOW);
        back.setCallback(new ActionCallback() {
            @Override
            public void run() {
                game.setGameState(new CheatMainMenu());
            }
        });

        MenuLabel label = new MenuLabel("auswaehlen_cheat");
        label.setColor(Color.LIGHT_GRAY);

        addEntry(new MenuSpacer(15));
        addEntry(back);
        addEntry(new MenuSpacer(30));
        addEntry(label);
        addEntry(new MenuSpacer(10));

        SaveData[] allData = SaveData.getAll(game);

        for (final SaveData data: allData) {
            MenuLoadSlot loadGameSlot = new MenuLoadSlot("slot", "slot_detail", data);
            loadGameSlot.setActiveColor(Color.GREEN);
            loadGameSlot.setCallback(new ActionCallback() {
                @Override
                public void run() {
                    game.setGameState(new CheatLevelSelection(data.getSlot()));
                }
            });

            addEntry(loadGameSlot);
        }

    }
}

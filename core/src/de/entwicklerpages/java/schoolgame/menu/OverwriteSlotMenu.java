package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.graphics.Color;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.SaveData;

/**
 * Sicherheitsfrage, ob ein vorhandener Spielstand wirklich überschrieben werden soll.
 *
 * @author nico
 */
public class OverwriteSlotMenu extends MenuState {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Der Speicherslot, der überschrieben werden soll.
     */
    private final SaveData saveData;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public OverwriteSlotMenu(SaveData data)
    {
        saveData = data;
    }

    @Override
    public String getStateName() {
        return "OVERWRITE_SLOT_MENU";
    }

    @Override
    String getI18nName() {
        return "GameMenu";
    }

    /**
     * Erstellt eine Menüstruktur und legt die Callbacks fest.
     */
    @Override
    void setupMenu() {

        MenuLabel label1 = new MenuLabel("ueberschreiben");
        label1.setColor(Color.ROYAL);

        MenuLabel label2 = new MenuLabel("verloren");
        label2.setColor(Color.GRAY);

        MenuEntry back = new MenuEntry("abbrechen");
        back.setActiveColor(Color.GREEN);
        back.setCallback(new ActionCallback() {
            @Override
            public void run() {
                game.setGameState(new NewGameMenu());
            }
        });

        MenuEntry overwrite = new MenuEntry("sicher");
        overwrite.setActiveColor(Color.RED);
        overwrite.setCallback(new ActionCallback() {
            @Override
            public void run() {
                saveData.reset();
                game.setGameState(new CreateGameSlot(saveData.getSlot()));
            }
        });

        addEntry(new MenuSpacer(40));
        addEntry(label1);
        addEntry(new MenuSpacer(10));
        addEntry(label2);
        addEntry(new MenuSpacer(100));
        addEntry(back);
        addEntry(new MenuSpacer(15));
        addEntry(overwrite);
    }
}

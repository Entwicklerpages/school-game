package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.graphics.Color;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.CheatManager;

/**
 * Das Cheatmenü. Sollte vom normalem Spieler nicht benutzt bzw. gefunden werden.
 *
 * @author nico
 */
public class CheatMainMenu extends MenuState {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String getStateName() {
        return "CHEAT_MAIN_MENU";
    }

    @Override
    String getI18nName() {
        return "Cheats";
    }

    /**
     * Erstellt die entsprechende Menüstruktur und legt die Callbacks fest.
     *
     * * Unsterblichkeit
     * * Schnelligkeit
     * * Speicherstand manipulieren
     * * Zurück
     */
    @Override
    void setupMenu() {
        MenuTitle title = new MenuTitle("titel");
        title.setColor(Color.PINK);

        final MenuEntry healthControl = new MenuEntry(CheatManager.getInstance().isHealthControlled() ? "gesundheit_on" : "gesundheit_off");
        healthControl.setCallback(new ActionCallback() {
            @Override
            public void run() {
                if (CheatManager.getInstance().isHealthControlled())
                {
                    healthControl.setLabel("gesundheit_off");
                    CheatManager.getInstance().setHealthControl(false);
                } else {
                    healthControl.setLabel("gesundheit_on");
                    CheatManager.getInstance().setHealthControl(true);
                }
            }
        });
        healthControl.setEnabled(!CheatManager.getInstance().isImmortal());

        final MenuEntry immortality = new MenuEntry(CheatManager.getInstance().isImmortal() ? "unsterblich_on" : "unsterblich_off");
        immortality.setCallback(new ActionCallback() {
            @Override
            public void run() {
                if (CheatManager.getInstance().isImmortal())
                {
                    immortality.setLabel("unsterblich_off");
                    CheatManager.getInstance().setImmortality(false);
                    healthControl.setEnabled(true);
                } else {
                    immortality.setLabel("unsterblich_on");
                    CheatManager.getInstance().setImmortality(true);
                    healthControl.setEnabled(false);
                    healthControl.setLabel("gesundheit_off");
                }
            }
        });

        final MenuEntry superfast = new MenuEntry(CheatManager.getInstance().isSuperFast() ? "superschnell_on" : "superschnell_off");
        superfast.setCallback(new ActionCallback() {
            @Override
            public void run() {
                if (CheatManager.getInstance().isSuperFast())
                {
                    superfast.setLabel("superschnell_off");
                    CheatManager.getInstance().setSuperFast(false);
                } else {
                    superfast.setLabel("superschnell_on");
                    CheatManager.getInstance().setSuperFast(true);
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
        addEntry(superfast);
        addEntry(healthControl);
        addEntry(modSaveData);
        addEntry(new MenuSpacer(25));
        addEntry(back);
    }
}

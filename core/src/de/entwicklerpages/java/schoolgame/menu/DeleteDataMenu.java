package de.entwicklerpages.java.schoolgame.menu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class DeleteDataMenu extends MenuState {
    @Override
    public String getStateName() {
        return "DELETE_DATA_MENU";
    }

    @Override
    String getI18nName() {
        return "OptionsMenu";
    }

    @Override
    void setupMenu() {
        MenuEntry back = new MenuEntry("abbrechen");
        back.setActiveColor(Color.GREEN);
        back.setCallback(new MenuCallback() {
            @Override
            public void run() {
                game.setGameState(new OptionsMenu());
            }
        });

        MenuEntry delete = new MenuEntry("wirklich_loeschen");
        delete.setActiveColor(Color.RED);
        delete.setCallback(new MenuCallback() {
            @Override
            public void run() {
                game.getPreferences().clear();
                game.getPreferences().flush();
                Gdx.app.exit();
            }
        });

        addEntry(back);
        addEntry(delete);
    }
}

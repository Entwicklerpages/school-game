package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.concurrent.TimeUnit;

import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.SaveData;

/**
 * Lässt den Spieler einen Slot für ein neues Spiel auswählen.
 *
 * @author nico
 */
public class NewGameMenu extends MenuState {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String getStateName() {
        return "NEW_GAME_MENU";
    }

    @Override
    String getI18nName() {
        return "GameMenu";
    }

    /**
     * Konfiguriert das Menü.
     * Es gibt eine Zurück-Taste und eine Übersicht über alle Slots.
     */
    @Override
    void setupMenu() {
        MenuEntry back = new MenuEntry("zurueck");
        back.setActiveColor(Color.YELLOW);
        back.setCallback(new ActionCallback() {
            @Override
            public void run() {
                game.setGameState(new MainMenu());
            }
        });

        MenuLabel label = new MenuLabel("auswaehlen_neu");
        label.setColor(Color.LIGHT_GRAY);

        addEntry(new MenuSpacer(15));
        addEntry(back);
        addEntry(new MenuSpacer(30));
        addEntry(label);
        addEntry(new MenuSpacer(10));

        SaveData[] allData = SaveData.getAll(game);

        for (final SaveData data: allData) {
            MenuNewSlot newGameSlot = new MenuNewSlot("slot", "slot_detail", data);
            newGameSlot.setActiveColor(Color.GREEN);
            newGameSlot.setDisabledColor(Color.RED);
            newGameSlot.setCallback(new ActionCallback() {
                @Override
                public void run() {
                    if (data.isUsed())
                    {
                        game.setGameState(new OverwriteSlotMenu(data));
                    } else {
                        game.setGameState(new CreateGameSlot(data.getSlot()));
                    }
                }
            });

            addEntry(newGameSlot);
        }

    }

    /**
     * Zeigt einen einzelnen Slot an.
     *
     * @author nico
     */
    class MenuNewSlot extends MenuEntry {
        private final GlyphLayout fontLayout;
        private BitmapFont font;
        private BitmapFont fontSmall;

        private final SaveData saveData;
        private final String detail;

        private int used = 0;
        private int id = 0;
        private final String playerName;
        private final String gender;
        private final String levelName;
        private String playTime;

        public MenuNewSlot(String label, String detail, SaveData saveData)
        {
            super(label);

            this.saveData = saveData;
            this.detail = detail;

            setCustomRendering(true);

            setHeight(100);

            used = saveData.isUsed() ? 1 : 0;
            id = saveData.getSlot().ordinal() + 1;
            playerName = saveData.getPlayerName();
            gender = saveData.isMale() ? "M" : "W";
            levelName = saveData.getLevelName();

            formatPlaytime(saveData.getPlayTime());

            fontLayout = new GlyphLayout();
        }

        private void formatPlaytime(long time)
        {
            time /= 1000L;
            long hours = TimeUnit.SECONDS.toHours(time);
            long minutes = TimeUnit.SECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(hours);
            long seconds = time - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);
            playTime = String.format("%02d:%02d:%02d",
                    hours,
                    minutes,
                    seconds
            );
        }

        public void render(OrthographicCamera camera, SpriteBatch batch, int y, MenuEntry activeEntry, SchoolGame game, I18NBundle localeBundle, float deltaTime) {

            Color entryColor = getColor();
            if (this == activeEntry)
            {
                entryColor = getActiveColor();
                if (used == 1)
                {
                    entryColor = getDisabledColor();
                }
            }

            if (font == null)
                font = game.getDefaultFont();

            if (fontSmall == null)
                fontSmall = game.getLongTextFont();

            fontLayout.setText(font, localeBundle.format(getLabel(), id, used, playerName, gender), entryColor, camera.viewportWidth, Align.center, false);
            font.draw(batch, fontLayout, -camera.viewportWidth / 2, y);

            fontLayout.setText(fontSmall, localeBundle.format(detail, used, levelName, playTime), entryColor, camera.viewportWidth, Align.center, false);
            fontSmall.draw(batch, fontLayout, -camera.viewportWidth / 2, y - 50);
        }
    }
}

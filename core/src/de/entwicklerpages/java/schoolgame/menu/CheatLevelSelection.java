package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Set;

import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.game.LevelManager;
import de.entwicklerpages.java.schoolgame.game.SaveData;

public class CheatLevelSelection extends LevelManager {

    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont smallFont;
    private GlyphLayout fontLayout;
    private I18NBundle localeBundle;

    private int selection = -1;
    private String[] levelIds;

    public CheatLevelSelection(SaveData.Slot slot)
    {
        super(slot);
    }

    @Override
    public void create(SchoolGame game) {
        this.game = game;

        if (lastSlot == null)
        {
            game.setGameState(new MainMenu());
            return;
        }

        saveData = new SaveData(game, lastSlot);

        batch = new SpriteBatch();
        font = game.getDefaultFont();
        smallFont = game.getLongTextFont();

        fontLayout = new GlyphLayout();

        FileHandle baseFileHandle = Gdx.files.internal("data/I18n/Cheats");
        localeBundle = I18NBundle.createBundle(baseFileHandle);

        Set<String> levelIdSet = levelMap.keySet();
        levelIds = levelIdSet.toArray(new String[levelIdSet.size()]);
    }

    @Override
    public void render(OrthographicCamera camera, float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        fontLayout.setText(font, localeBundle.get("zurueck"), selection == -1 ? Color.YELLOW : Color.WHITE, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, camera.viewportHeight / 2 - 20);

        for (int i = 0; i < levelIds.length; i++)
        {
            fontLayout.setText(smallFont, levelIds[i], selection == i ? Color.GREEN : Color.WHITE, camera.viewportWidth, Align.center, false);
            smallFont.draw(batch, fontLayout, -camera.viewportWidth / 2, camera.viewportHeight / 2 - (i * 30 + 100));
        }

        batch.end();
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.ESCAPE)
        {
            game.setGameState(new CheatSlotSelectionMenu());
            return true;
        }

        if (keycode == Input.Keys.UP || keycode == Input.Keys.W)
        {
            selection--;
            if (selection < -1) selection = levelIds.length - 1;

            return true;
        }

        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
        {
            selection++;
            if (selection >= levelIds.length) selection = -1;

            return true;
        }

        if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE)
        {
            if (selection >= 0)
            {
                Gdx.app.log("CHEAT", "Set level ID to " + levelIds[selection]);
                saveData.setLevelId(levelIds[selection]);
                saveData.setLevelName(localeBundle.get("modifiziert"));
                saveData.save(null);
            }
            game.setGameState(new CheatSlotSelectionMenu());
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        return false;
    }

    @Override
    public String getStateName() {
        return "CHEAT_LEVEL_SELECTION";
    }
}

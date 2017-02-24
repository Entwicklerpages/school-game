package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.game.LevelManager;
import de.entwicklerpages.java.schoolgame.game.SaveData;

public class CheatLevelSelection extends LevelManager {

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
    }

    @Override
    public void render(OrthographicCamera camera, float deltaTime) {
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

package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.StringBuilder;

import de.entwicklerpages.java.schoolgame.GameState;
import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.common.InputHelper;
import de.entwicklerpages.java.schoolgame.game.LevelManager;
import de.entwicklerpages.java.schoolgame.game.SaveData;

public class CreateGameSlot implements GameState, InputProcessor {

    private final static String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzäöü";

    private SaveData saveData;
    private SchoolGame game;
    private SaveData.Slot slot;

    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont smallFont;
    private GlyphLayout fontLayout;
    private I18NBundle localeBundle;

    private boolean stateSwitch = false;
    private StringBuilder playerName = new StringBuilder(20);
    private String playerNameStroke = "|";
    private float strokeTimer = 0;

    private SetupStep setupStep;

    public CreateGameSlot(SaveData.Slot slot)
    {
        this.slot = slot;
        setupStep = SetupStep.STEP_1_NAME;
    }

    @Override
    public void create(SchoolGame game) {
        this.game = game;

        saveData = new SaveData(this.game, this.slot);

        batch = new SpriteBatch();
        font = game.getDefaultFont();
        smallFont = game.getLongTextFont();

        fontLayout = new GlyphLayout();

        FileHandle baseFileHandle = Gdx.files.internal("data/I18n/GameMenu");
        localeBundle = I18NBundle.createBundle(baseFileHandle);
    }

    @Override
    public void render(OrthographicCamera camera, float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        fontLayout.setText(smallFont, localeBundle.get("esc_abbrechen"), Color.LIGHT_GRAY, 50, Align.left, false);
        smallFont.draw(batch, fontLayout, -camera.viewportWidth / 2 + 5, camera.viewportHeight / 2 - 10);

        fontLayout.setText(font, localeBundle.get("erstellen"), Color.CORAL, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, camera.viewportHeight / 2 - 50);

        switch (setupStep)
        {
            case STEP_1_NAME:
                renderName(camera, deltaTime);
                break;
            case STEP_2_GENDER:
                renderGender(camera);
                break;
            case STEP_3_TUTORIAL:
                renderTutorial(camera);
                break;
            case STEP_4_START:
                renderStart(camera);
                break;
        }

        batch.end();
    }

    public void renderName(OrthographicCamera camera, float deltaTime)
    {
        strokeTimer += deltaTime;

        if (strokeTimer > 0.6f)
        {
            strokeTimer = 0f;
            stateSwitch = !stateSwitch;
            playerNameStroke = String.format("%s%s", playerName, stateSwitch ? " " : "|");
        }

        fontLayout.setText(font, localeBundle.get("name_eingeben"), Color.WHITE, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, camera.viewportHeight / 2 - 120);

        fontLayout.setText(font, playerNameStroke, Color.ROYAL, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, 30);

        fontLayout.setText(smallFont, localeBundle.get("enter_bestaetigen"), Color.WHITE, camera.viewportWidth, Align.center, false);
        smallFont.draw(batch, fontLayout, -camera.viewportWidth / 2, -camera.viewportHeight / 2 + 70);
    }

    public void renderGender(OrthographicCamera camera)
    {
        Color maleColor;
        Color femaleColor;

        if (stateSwitch)
        {
            maleColor = Color.GREEN;
            femaleColor = Color.DARK_GRAY;
        } else {
            maleColor = Color.DARK_GRAY;
            femaleColor = Color.GREEN;
        }

        fontLayout.setText(font, localeBundle.get("geschlecht"), Color.WHITE, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, camera.viewportHeight / 2 - 120);

        fontLayout.setText(font, localeBundle.get("frau"), femaleColor, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, 30);

        fontLayout.setText(font, localeBundle.get("mann"), maleColor, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, -30);

        fontLayout.setText(smallFont, localeBundle.get("enter_bestaetigen"), Color.WHITE, camera.viewportWidth, Align.center, false);
        smallFont.draw(batch, fontLayout, -camera.viewportWidth / 2, -camera.viewportHeight / 2 + 70);
    }

    public void renderTutorial(OrthographicCamera camera)
    {
        Color yesColor;
        Color noColor;

        if (stateSwitch)
        {
            yesColor = Color.GREEN;
            noColor = Color.DARK_GRAY;
        } else {
            yesColor = Color.DARK_GRAY;
            noColor = Color.GREEN;
        }

        fontLayout.setText(font, localeBundle.get("tutorial1"), Color.WHITE, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, camera.viewportHeight / 2 - 120);
        fontLayout.setText(font, localeBundle.get("tutorial2"), Color.WHITE, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, camera.viewportHeight / 2 - 185);

        fontLayout.setText(font, localeBundle.get("ja"), yesColor, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, 30);

        fontLayout.setText(font, localeBundle.get("nein"), noColor, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, -30);

        fontLayout.setText(smallFont, localeBundle.get("enter_bestaetigen"), Color.WHITE, camera.viewportWidth, Align.center, false);
        smallFont.draw(batch, fontLayout, -camera.viewportWidth / 2, -camera.viewportHeight / 2 + 70);
    }

    public void renderStart(OrthographicCamera camera)
    {
        fontLayout.setText(font, localeBundle.format("hallo", saveData.getPlayerName(), saveData.isMale() ? "M" : "W"), Color.WHITE, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, 100);

        fontLayout.setText(font, localeBundle.get("fertig1"), Color.WHITE, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, 50);

        fontLayout.setText(font, localeBundle.get("fertig2"), Color.WHITE, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, -50);
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public String getStateName() {
        return "CREATE_GAME_SLOT";
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE)
        {
            saveData.reset();
            game.setGameState(new NewGameMenu());
            return true;
        }

        if (keycode == Input.Keys.ENTER)
        {
            switch (setupStep)
            {
                case STEP_1_NAME:
                    handleAcceptName();
                    break;
                case STEP_2_GENDER:
                    handleAcceptGender();
                    break;
                case STEP_3_TUTORIAL:
                    handleAcceptTutorial();
                    break;
                case STEP_4_START:
                    handleAcceptStart();
                    break;
            }
            return true;
        }

        if (setupStep == SetupStep.STEP_2_GENDER || setupStep == SetupStep.STEP_3_TUTORIAL)
        {
            if (InputHelper.checkKeys(keycode, Input.Keys.UP, Input.Keys.W, Input.Keys.DOWN, Input.Keys.S))
            {
                stateSwitch = !stateSwitch;
                return true;
            }
        }

        if (setupStep == SetupStep.STEP_1_NAME && keycode == Input.Keys.BACKSPACE && playerName.length() > 0)
        {
            playerName.deleteCharAt(playerName.length() - 1);
            strokeTimer = 10f; // Trigger a new string creation.
            return true;
        }

        return false;
    }

    private void handleAcceptName()
    {
        if (playerName.length() < 2)
        {
            return;
        }

        saveData.setPlayerName(playerName.toString());
        setupStep = SetupStep.STEP_2_GENDER;
        stateSwitch = false;
    }

    private void handleAcceptGender()
    {
        saveData.setMale(stateSwitch);
        setupStep = SetupStep.STEP_3_TUTORIAL;
        stateSwitch = true;
    }

    private void handleAcceptTutorial()
    {
        if (stateSwitch) {
            saveData.setLevelId("tutorial");
        } else {
            saveData.setLevelId("introduction");
        }

        saveData.setLevelName("Start");

        setupStep = SetupStep.STEP_4_START;
    }

    private void handleAcceptStart()
    {
        saveData.save(null);
        game.setGameState(new LevelManager(slot));
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        if (setupStep != SetupStep.STEP_1_NAME || playerName.length() >= 20) return false;

        String input = String.valueOf(character).toLowerCase();

        if (ALLOWED_CHARS.contains(input))
        {
            if (playerName.length() == 0)
            {
                input = input.toUpperCase();
            }
            playerName.append(input);
            strokeTimer = 10f; // Trigger a new string creation.
            return true;
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    enum SetupStep
    {
        STEP_1_NAME,
        STEP_2_GENDER,
        STEP_3_TUTORIAL,
        STEP_4_START
    }
}

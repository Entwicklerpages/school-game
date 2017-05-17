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

import java.util.Arrays;
import java.util.Set;

import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.game.LevelManager;
import de.entwicklerpages.java.schoolgame.game.SaveData;

/**
 * Teil des Cheat-Menüs.
 *
 * Erlaubt einem ENTWICKLER ein beliebiges Level zu wählen.
 *
 * Erweitert den LevelManager. Dieser zeigt zwar eigentlich das Spiel an, allerdings ist er in der Lage
 * eine Liste aller vorhandenen Level zu erstellen. Diese Liste wird hier gebraucht.
 *
 * @author nico
 */
public class CheatLevelSelection extends LevelManager {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static final int MAX_ROWS = 16;

    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont smallFont;
    private GlyphLayout fontLayout;
    private I18NBundle localeBundle;

    private int selection = -1;
    private int cols = 1;
    private String[] levelIds;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Konstruktor.
     *
     * @param slot der Slot, der verändert werden soll
     */
    public CheatLevelSelection(SaveData.Slot slot)
    {
        super(slot);
    }

    /**
     * Initialisierung
     *
     * Wird automatisch aufgerufen.
     * Erfasst auch gleich die Liste aller Level.
     *
     * @param game zeigt auf das SchoolGame, dass das Spiel verwaltet.
     */
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

        Arrays.sort(levelIds);


        cols = (levelIds.length - (levelIds.length % MAX_ROWS)) / MAX_ROWS;
        if (levelIds.length % MAX_ROWS > 0)
            cols++;

    }

    /**
     * Zeigt die Liste aller Level an.
     *
     * @param camera  die aktuelle Kamera
     * @param deltaTime die vergangene Zeit seit dem letztem Frame
     */
    @Override
    public void render(OrthographicCamera camera, float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        fontLayout.setText(font, localeBundle.get("zurueck"), selection == -1 ? Color.YELLOW : Color.WHITE, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, camera.viewportHeight / 2 - 20);

        float width = camera.viewportWidth / ((float)cols);

        for (int j = 0; j < cols; j++)
        {
            for (int i = 0; i < MAX_ROWS && (j * MAX_ROWS) + i < levelIds.length; i++)
            {
                int index = (j * MAX_ROWS) + i;

                fontLayout.setText(smallFont, levelIds[index], selection == index ? Color.GREEN : Color.WHITE, width, Align.center, false);
                smallFont.draw(batch, fontLayout, -camera.viewportWidth / 2 + width * j, camera.viewportHeight / 2 - (i * 30 + 100));
            }
        }

        batch.end();
    }

    /**
     * Überschreibt die update Methode des LevelManagers, damit dieser kein Level anzeigen kann.
     *
     * @see LevelManager#update(float)
     *
     * @param deltaTime die vergangene Zeit seit dem letztem Frame
     */
    @Override
    public void update(float deltaTime) {
    }

    /**
     * Erlaubt die Navigation durch die Liste der Level und
     * ermöglicht eine Auswahl.
     *
     * @param keycode der Tastencode, der gedrückten Taste
     * @return true, wenn auf das Ereignis reagiert wurde
     */
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

    /**
     * Wird eigentlich nicht gebraucht, aber wir verhindern das der LevelManager hier etwas machen kann.
     *
     * @param keycode der Tastencode, der gedrückten Taste
     * @return true, wenn auf das Ereignis reagiert wurde
     */
    @Override
    public boolean keyUp(int keycode) {

        return false;
    }

    /**
     * Name dieses Zustands
     *
     * @return der Name
     */
    @Override
    public String getStateName() {
        return "CHEAT_LEVEL_SELECTION";
    }
}

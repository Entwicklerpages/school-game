package de.entwicklerpages.java.schoolgame.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import de.entwicklerpages.java.schoolgame.SchoolGame;

/**
 * Stellt im Spiel ein Menü dar.
 * Ist eng mit der {@link Level} Klasse verknüpft.
 *
 * @author nico
 */
public class IngameMenu {

    private static final String[] ENTRIES = new String[]{"weiter", "mainmenu", "beenden"};
    private static final float WIDTH = 450f;
    private static final float HEIGHT = 420f;

    private SchoolGame game;
    private Level level;
    private BitmapFont titleFont;
    private BitmapFont textFont;
    private BitmapFont smallFont;
    private GlyphLayout fontLayout;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private I18NBundle localeBundle;
    private Matrix4 projection;

    private int activeEntry = 0;
    private MenuMode menuMode = MenuMode.MODE_PAUSE;


    public IngameMenu(SchoolGame game, Level level)
    {
        this.game = game;
        this.level = level;

        titleFont = game.getTitleFont();
        textFont = game.getDefaultFont();
        smallFont = game.getLongTextFont();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        localeBundle = level.getLocaleBundle();

        fontLayout = new GlyphLayout();

        projection = new Matrix4();
    }

    public void reset()
    {
        menuMode = MenuMode.MODE_PAUSE;
        activeEntry = 0;
    }

    public boolean handleInput(int keycode)
    {
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W)
        {
            activeEntry--;

            if (menuMode == MenuMode.MODE_PAUSE && activeEntry < 0) activeEntry = ENTRIES.length - 1;
            if (menuMode != MenuMode.MODE_PAUSE && activeEntry < 0) activeEntry = 1;

            return true;
        }

        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
        {
            activeEntry++;

            if (menuMode == MenuMode.MODE_PAUSE && activeEntry >= ENTRIES.length) activeEntry = 0;
            if (menuMode != MenuMode.MODE_PAUSE && activeEntry >= 2) activeEntry = 0;

            return true;
        }

        if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE)
        {
            if (menuMode == MenuMode.MODE_PAUSE) {
                switch (activeEntry) {
                    case 0:
                        level.setPlaying();
                        break;
                    case 1:
                        menuMode = MenuMode.MODE_MAIN_MENU;
                        activeEntry = 0;
                        break;
                    case 2:
                        menuMode = MenuMode.MODE_QUIT;
                        activeEntry = 0;
                        break;
                }
            } else {
                if (activeEntry == 1)
                {
                    if (menuMode == MenuMode.MODE_MAIN_MENU)
                    {
                        level.exitToMenu();
                    } else {
                        Gdx.app.exit();
                    }
                } else {
                    reset();
                }
            }
            return true;
        }

        return false;
    }

    public void render(OrthographicCamera camera)
    {
        // Kameraprojektion anpassen
        projection.set(camera.projection).translate(-camera.viewportWidth / 2f, -camera.viewportHeight / 2f, 0f);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float x = camera.viewportWidth / 2f - WIDTH / 2f;
        float y = camera.viewportHeight / 2f - HEIGHT / 2f;

        shapeRenderer.setProjectionMatrix(projection);

        /////////////////

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.0f, 0.0f, 0.0f, 0.4f);
        shapeRenderer.rect(0f, 0f, camera.viewportWidth, camera.viewportHeight);

        shapeRenderer.setColor(0.0f, 0.1f, 0.15f, 0.75f);
        shapeRenderer.rect(x, y, WIDTH, HEIGHT);

        shapeRenderer.end();

        /////////////////

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(x, y, WIDTH, HEIGHT);

        shapeRenderer.end();

        //////////////////

        Gdx.gl.glDisable(GL20.GL_BLEND);

        if (menuMode == MenuMode.MODE_PAUSE)
            renderPause(camera, x);
        else
            renderQuestion(camera, x);
    }

    private void renderPause(OrthographicCamera camera, float x)
    {
        batch.setProjectionMatrix(projection);
        batch.begin();

        float y = camera.viewportHeight - (camera.viewportHeight - HEIGHT) / 2f;

        y -= 45;

        fontLayout.setText(titleFont, localeBundle.get("menu"), Color.CORAL, WIDTH, Align.center, false);
        titleFont.draw(batch, fontLayout, x, y);

        y -= 125f;

        for (int i = 0; i < ENTRIES.length; i++)
        {
            Color entryColor = Color.WHITE;
            if (i == activeEntry)
            {
                entryColor = Color.YELLOW;
            }

            fontLayout.setText(textFont, localeBundle.get(ENTRIES[i]), entryColor, WIDTH, Align.center, false);
            textFont.draw(batch, fontLayout, x, y);

            y -= 75f;
        }

        batch.end();
    }

    private void renderQuestion(OrthographicCamera camera, float x)
    {
        batch.setProjectionMatrix(projection);
        batch.begin();

        float y = camera.viewportHeight - (camera.viewportHeight - HEIGHT) / 2f;

        y -= 45;

        fontLayout.setText(textFont, localeBundle.get("sicher1"), Color.CORAL, WIDTH, Align.center, false);
        textFont.draw(batch, fontLayout, x, y);

        y -= 60f;

        fontLayout.setText(smallFont, localeBundle.get("sicher2"), Color.CORAL, WIDTH, Align.center, false);
        smallFont.draw(batch, fontLayout, x, y);

        y -= 30f;

        fontLayout.setText(smallFont, localeBundle.get("sicher3"), Color.CORAL, WIDTH, Align.center, false);
        smallFont.draw(batch, fontLayout, x, y);

        y -= 135f;

        fontLayout.setText(textFont, localeBundle.get("abbrechen"), activeEntry == 0 ? Color.YELLOW : Color.WHITE, WIDTH, Align.center, false);
        textFont.draw(batch, fontLayout, x, y);

        y -= 70f;

        fontLayout.setText(textFont, localeBundle.get(menuMode == MenuMode.MODE_MAIN_MENU ? "ins_menu" : "beenden"), activeEntry == 1 ? Color.YELLOW : Color.WHITE, WIDTH, Align.center, false);
        textFont.draw(batch, fontLayout, x, y);

        batch.end();
    }

    private enum MenuMode
    {
        MODE_PAUSE,
        MODE_QUIT,
        MODE_MAIN_MENU
    }
}

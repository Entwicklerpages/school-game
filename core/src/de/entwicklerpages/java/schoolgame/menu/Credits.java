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

import java.util.ArrayList;
import java.util.List;

import de.entwicklerpages.java.schoolgame.GameState;
import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.common.InputHelper;

/**
 * Klasse zum Anzeigen der Credits.
 *
 * @author nico
 */
public class Credits implements GameState, InputProcessor {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static final int FONT_HEIGHT = 55;
    private static final int SCROLL_SPEED = 60;

    private SpriteBatch batch;
    private SchoolGame game;

    private BitmapFont font;
    private GlyphLayout fontLayout;

    private float offset = 0;
    private float timer = 1.5f;

    private List<CreditLine>creditLines = new ArrayList<CreditLine>();

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Initialisierung.
     *
     * @param game zeigt auf das SchoolGame, dass das Spiel verwaltet
     */
    @Override
    public void create(SchoolGame game) {
        this.game = game;
        batch = new SpriteBatch();

        font = game.getDefaultFont();

        fontLayout = new GlyphLayout();

        offset -= Gdx.graphics.getHeight() / 2 - 35;

        FileHandle credits = Gdx.files.internal("data/misc/credits.txt");

        if (credits.exists() && !credits.isDirectory())
        {
            prepareCredits(credits);
        }
    }

    /**
     * Zeigt die Credits an.
     *
     * @param camera  die aktuelle Kamera
     * @param deltaTime die vergangene Zeit seit dem letztem Frame
     */
    @Override
    public void render(OrthographicCamera camera, float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        fontLayout.setText(font, "ESC", Color.DARK_GRAY, 50, Align.left, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2 + 5, camera.viewportHeight / 2 - 10);

        int y = (int) camera.viewportHeight / 2 - 35 + (int)offset;

        for (CreditLine line: creditLines) {

            fontLayout.setText(font, line.getLine(), line.getColor(), camera.viewportWidth, Align.center, false);
            font.draw(batch, fontLayout, -camera.viewportWidth / 2, y);
            y -= FONT_HEIGHT;
        }

        batch.end();
    }

    /**
     * Lässt die Texte nach oben scrollen.
     * Wechselt am Ende zurück ins Hauptmenü.
     *
     * @param deltaTime die vergangene Zeit seit dem letztem Frame
     */
    @Override
    public void update(float deltaTime) {

        if (timer > 0)
        {
            timer -= deltaTime;
        }
        else
        {
            offset += deltaTime * SCROLL_SPEED;

            if (offset > creditLines.size() * FONT_HEIGHT + 80) {
                game.setGameState(new MainMenu());
            }
        }
    }

    /**
     * Aufräumarbeiten.
     */
    @Override
    public void dispose() {
        batch.dispose();

        creditLines.clear();
    }

    @Override
    public String getStateName() {
        return "CREDITS";
    }

    /**
     * Bereitet die Credits für die Anzeige vor.
     *
     * Zeilen, die mit # Anfangen werden als Überschriften gehandhabt.
     * Zeilen, die mit !# Anfangen, werden als Haupt-Überschriften gehandhabt.
     *
     * @param creditsFile ein FileHandle auf die Datei mit den Credits
     */
    private void prepareCredits(FileHandle creditsFile)
    {
        creditLines.clear();

        String credits = creditsFile.readString();

        String[] lines = credits.split("\n");

        for (String line: lines) {
            line = line.trim();
            int header = 0;

            if (line.startsWith("#"))
            {
                header = 1;
                line = line.substring(1).trim();
            }

            if (line.startsWith("!#"))
            {
                header = 2;
                line = line.substring(2).trim();
            }

            creditLines.add(new CreditLine(line, header));
        }
    }

    /**
     * Bricht ab, wenn auf Escape gedrückt wird.
     *
     * @param keycode der Tastencode der gedrückten Taste
     * @return true, wenn auf das Ereignis reagiert wurde
     */
    @Override
    public boolean keyDown(int keycode) {

        if (InputHelper.checkKeys(keycode, Input.Keys.ESCAPE, Input.Keys.SPACE))
        {
            game.setGameState(new MainMenu());
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
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

    /**
     * Speichert eine einzige Zeile der Credits.
     *
     * @author nico
     */
    protected class CreditLine {
        private final String line;
        private boolean heading;
        private boolean topHeading;

        /**
         * Konstruktor.
         *
         * @param line der Text der Zeile
         * @param heading handelt es sich um eine Überschrift?
         */
        public CreditLine(String line, int heading) {
            this.line = line;

            this.heading = false;
            this.topHeading = false;

            switch (heading)
            {
                case 1:
                    this.heading = true;
                    break;
                case 2:
                    this.topHeading = true;
                    break;
            }
        }

        /**
         * Gibt den Text der Zeile zurück.
         *
         * @return der Text
         */
        public String getLine() {
            return line;
        }

        /**
         * Gibt die Farbe der Zeile zurück.
         *
         * @return die Farbe
         */
        public Color getColor() {
            if (heading)
            {
                return Color.LIME;
            }
            if (topHeading)
            {
                return Color.GOLDENROD;
            }
            return Color.WHITE;
        }
    }
}

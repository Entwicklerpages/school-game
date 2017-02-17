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

import java.util.ArrayList;
import java.util.List;

import de.entwicklerpages.java.schoolgame.GameState;
import de.entwicklerpages.java.schoolgame.SchoolGame;

public abstract class MenuState implements GameState, InputProcessor {

    private List<MenuEntry> entries;
    private MenuEntry activeEntry;

    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout fontLayout;
    private I18NBundle localeBundle;

    protected SchoolGame game;

    abstract void setupMenu();
    abstract String getI18nName();

    @Override
    public void create(SchoolGame game) {
        Gdx.app.getApplicationLogger().log("INFO", "Menu init...");

        this.game = game;
        entries = new ArrayList<MenuEntry>();
        this.setupMenu();

        batch = new SpriteBatch();
        font = game.getDefaultFont();

        fontLayout = new GlyphLayout();

        FileHandle baseFileHandle = Gdx.files.internal("I18n/" + getI18nName());
        localeBundle = I18NBundle.createBundle(baseFileHandle);

        Gdx.app.getApplicationLogger().log("INFO", "Menu finished...");
    }

    @Override
    public void render(OrthographicCamera camera, float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // TODO: Bessere Positionierung von Einträgen

        int y = (int) camera.viewportHeight / 2 - 35;

        for (MenuEntry entry: entries) {
            Color entryColor = entry.getColor();

            if (entry == activeEntry)
            {
                entryColor = entry.getActiveColor();
            }

            fontLayout.setText(font, localeBundle.get(entry.getLabel()), entryColor, camera.viewportWidth, Align.center, false);
            font.draw(batch, fontLayout, -camera.viewportWidth / 2, y);
            y -= 65;
        }

        batch.end();
    }

    @Override
    public void update(float deltaTime) {
    }

    protected final void addEntry(MenuEntry entry)
    {
        if (entry == null) return; // ArrayList erlaubt null. Wir wollen das aber nicht.
        entries.add(entry);

        if (entries.size() == 1) // Der erste Eintrag wird automatisch ausgewählt.
        {
            activeEntry = entry;
        }
    }

    protected final void nextEntry()
    {
        int index = entries.indexOf(activeEntry);

        index++;

        if (index >= entries.size()) index = 0;

        activeEntry = entries.get(index);
    }

    protected final void previousEntry()
    {
        int index = entries.indexOf(activeEntry);

        index--;

        if (index < 0) index = entries.size() - 1;

        activeEntry = entries.get(index);
    }

    @Override
    public void dispose() {
        Gdx.app.getApplicationLogger().log("INFO", "Menu dispose...");

        entries.clear();

        batch.dispose();

        Gdx.app.getApplicationLogger().log("INFO", "Menu leave.");
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.UP || keycode == Input.Keys.W)
        {
            previousEntry();
            return true;
        }

        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
        {
            nextEntry();
            return true;
        }

        if (keycode == Input.Keys.ENTER || keycode == Input.Keys.BUTTON_A)
        {
            if (activeEntry.getCallback() != null)
            {
                activeEntry.getCallback().run();
            }
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

    class MenuEntry {
        private MenuCallback callback;
        private String label;
        private Color color = Color.WHITE;
        private Color activeColor = Color.RED;

        public MenuEntry()
        {
            callback = null;
            this.setLabel("");
        }

        public MenuEntry(String label)
        {
            this.setLabel(label);
        }

        public MenuCallback getCallback() {
            return callback;
        }

        public void setCallback(MenuCallback callback) {
            this.callback = callback;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public Color getActiveColor() {
            return activeColor;
        }

        public void setActiveColor(Color activeColor) {
            this.activeColor = activeColor;
        }
    }

    interface MenuCallback {
        void run();
    }
}

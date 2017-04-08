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

import de.entwicklerpages.java.schoolgame.AudioManager;
import de.entwicklerpages.java.schoolgame.GameState;
import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.common.InputHelper;

public abstract class MenuState implements GameState, InputProcessor {

    private List<MenuEntry> entries;
    private MenuEntry activeEntry;

    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout fontLayout;
    private I18NBundle localeBundle;

    private AudioManager.SoundKey selectSound;
    private AudioManager.SoundKey changeSound;

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

        selectSound = game.getAudioManager().createSound("menu", "select.wav", true);
        changeSound = game.getAudioManager().createSound("menu", "change.wav", true);

        FileHandle baseFileHandle = Gdx.files.internal("I18n/" + getI18nName());
        localeBundle = I18NBundle.createBundle(baseFileHandle);

        Gdx.app.getApplicationLogger().log("INFO", "Menu finished...");
    }

    @Override
    public void render(OrthographicCamera camera, float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        int y = (int) camera.viewportHeight / 2 - 35;

        for (MenuEntry entry: entries) {

            if (entry.isCustomRendering())
            {
                entry.render(camera, batch, y, activeEntry, game, localeBundle, deltaTime);
            } else {
                defaultRender(camera, entry, y);
            }

            y -= entry.getHeight();
        }

        batch.end();
    }

    protected void defaultRender(OrthographicCamera camera, MenuEntry entry, int y)
    {
        Color entryColor = entry.getColor();
        if (entry == activeEntry)
        {
            entryColor = entry.getActiveColor();
        }
        if (!entry.isEnabled())
        {
            entryColor = entry.getDisabledColor();
        }

        fontLayout.setText(font, localeBundle.get(entry.getLabel()), entryColor, camera.viewportWidth, Align.center, false);
        font.draw(batch, fontLayout, -camera.viewportWidth / 2, y);
    }

    @Override
    public void update(float deltaTime) {
    }

    protected final void addEntry(MenuEntry entry)
    {
        if (entry == null) return; // ArrayList erlaubt null. Wir wollen das aber nicht.
        entries.add(entry);

        if (activeEntry == null && entry.isEnabled()) // Der erste Eintrag wird automatisch ausgewählt.
        {
            activeEntry = entry;
        }
    }

    protected final void nextEntry()
    {
        if (checkAllDisabled())
        {
            activeEntry = null;
            return;
        }

        if (activeEntry == null && entries.size() > 0)
        {
            activeEntry = entries.get(0);
        }

        int index = entries.indexOf(activeEntry);

        index++;

        if (index >= entries.size()) index = 0;

        activeEntry = entries.get(index);

        if (!activeEntry.isEnabled())
        {
            nextEntry();
        }
    }

    protected final void previousEntry()
    {
        if (checkAllDisabled())
        {
            activeEntry = null;
            return;
        }

        if (activeEntry == null && entries.size() > 0)
        {
            activeEntry = entries.get(0);
        }

        int index = entries.indexOf(activeEntry);

        index--;

        if (index < 0) index = entries.size() - 1;

        activeEntry = entries.get(index);

        if (!activeEntry.isEnabled())
        {
            previousEntry();
        }
    }

    private final boolean checkAllDisabled()
    {
        boolean allDisabled = true;
        for (MenuEntry entry : entries)
        {
            allDisabled = allDisabled && !entry.isEnabled();
        }

        return allDisabled;
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

        if (InputHelper.checkKeys(keycode, Input.Keys.UP, Input.Keys.W))
        {
            game.getAudioManager().playSound(changeSound);
            previousEntry();
            return true;
        }

        if (InputHelper.checkKeys(keycode, Input.Keys.DOWN, Input.Keys.S))
        {
            game.getAudioManager().playSound(changeSound);
            nextEntry();
            return true;
        }

        if (InputHelper.checkKeys(keycode, Input.Keys.ENTER, Input.Keys.SPACE))
        {
            game.getAudioManager().playSound(selectSound);
            if (activeEntry != null && activeEntry.isEnabled() && activeEntry.getCallback() != null)
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
        private ActionCallback callback = null;
        private String label;
        private boolean enabled = true;
        private boolean customRendering = false;
        private Color color = Color.WHITE;
        private Color activeColor = Color.RED;
        private Color disabledColor = Color.GRAY;
        private int height = 65;

        public MenuEntry()
        {
            this.setLabel("");
        }

        public MenuEntry(String label)
        {
            this.setLabel(label);
        }

        public ActionCallback getCallback() {
            return callback;
        }

        public void setCallback(ActionCallback callback) {
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

        public Color getDisabledColor() {
            return disabledColor;
        }

        public void setDisabledColor(Color disabledColor) {
            this.disabledColor = disabledColor;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isCustomRendering() {
            return customRendering;
        }

        protected void setCustomRendering(boolean customRendering) {
            this.customRendering = customRendering;
        }

        public int getHeight() {
            return height;
        }

        protected void setHeight(int height) {
            this.height = height;
        }

        public void render(OrthographicCamera camera, SpriteBatch batch, int y, MenuEntry activeEntry, SchoolGame game, I18NBundle localeBundle, float deltaTime) {
        }
    }

    class MenuSpacer extends MenuEntry {

        public MenuSpacer(int height)
        {
            setEnabled(false);
            setCustomRendering(true);
            setHeight(height);
        }
    }

    class MenuLabel extends MenuEntry {

        private GlyphLayout fontLayout;
        private BitmapFont font;

        public MenuLabel(String label)
        {
            super(label);

            setEnabled(false);
            setCustomRendering(true);

            fontLayout = new GlyphLayout();
        }

        public void render(OrthographicCamera camera, SpriteBatch batch, int y, MenuEntry activeEntry, SchoolGame game, I18NBundle localeBundle, float deltaTime) {

            if (font == null)
                font = game.getDefaultFont();

            fontLayout.setText(font, localeBundle.get(getLabel()), getColor(), camera.viewportWidth, Align.center, false);
            font.draw(batch, fontLayout, -camera.viewportWidth / 2, y);
        }
    }

    class MenuTitle extends MenuEntry {

        private GlyphLayout fontLayout;
        private BitmapFont font;

        public MenuTitle(String label) {
            super(label);

            setEnabled(false);
            setCustomRendering(true);
            setHeight(85);

            fontLayout = new GlyphLayout();
        }

        public void render(OrthographicCamera camera, SpriteBatch batch, int y, MenuEntry activeEntry, SchoolGame game, I18NBundle localeBundle, float deltaTime) {

            if (font == null)
                font = game.getTitleFont();

            fontLayout.setText(font, localeBundle.get(getLabel()), getColor(), camera.viewportWidth, Align.center, false);
            font.draw(batch, fontLayout, -camera.viewportWidth / 2, y);
        }
    }
}

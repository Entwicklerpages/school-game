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
import de.entwicklerpages.java.schoolgame.common.InputManager;

/**
 * Vorlage für die meisten Menüs im Spiel.
 * Definiert grundlegende Abläufe wie das Navigieren durch Menüs und das Auswählen eines Eintrags.
 *
 * @author nico
 */
public abstract class MenuState implements GameState, InputProcessor {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private List<MenuEntry> entries;
    private MenuEntry activeEntry;

    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout fontLayout;
    private I18NBundle localeBundle;

    private AudioManager.SoundKey selectSound;
    private AudioManager.SoundKey changeSound;

    protected SchoolGame game;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Konfiguriert das Menü.
     */
    abstract void setupMenu();

    /**
     * Name der Sprachdatei, die geladen werden soll.
     *
     * @return der Name de Sprachdatei
     */
    abstract String getI18nName();

    /**
     * Initialisierung.
     *
     * @param game zeigt auf das SchoolGame, dass das Spiel verwaltet
     */
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

        FileHandle baseFileHandle = Gdx.files.internal("data/I18n/" + getI18nName());
        localeBundle = I18NBundle.createBundle(baseFileHandle);

        Gdx.app.getApplicationLogger().log("INFO", "Menu finished...");
    }

    /**
     * Zeigt das Menü an.
     *
     * @param camera  die aktuelle Kamera
     * @param deltaTime die vergangene Zeit seit dem letztem Frame
     */
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

    /**
     * Zeigt Menüeinträge ohne besondere Render-Methode an.
     *
     * @param camera die aktive Kamera
     * @param entry der Menüeintrag, der angezeigt werden soll
     * @param y die Y-Position des Eintrags
     */
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

    /**
     * Macht nix, muss aber da sein.
     *
     * @param deltaTime die vergangene Zeit seit dem letztem Frame
     */
    @Override
    public void update(float deltaTime) {
    }

    /**
     * Fügt einen Eintrag am Ende hinzu.
     *
     * @param entry der neue Eintrag
     */
    protected final void addEntry(MenuEntry entry)
    {
        if (entry == null) return; // ArrayList erlaubt null. Wir wollen das aber nicht.
        entries.add(entry);

        if (activeEntry == null && entry.isEnabled()) // Der erste Eintrag wird automatisch ausgewählt.
        {
            activeEntry = entry;
        }
    }

    /**
     * Geht einen Eintrag nach vorne.
     */
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

    /**
     * Geht einen Eintrag zurück.
     */
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

    /**
     * Prüft, ob alle Menüeinträge deaktiviert sind oder ob man einen auswählen kann.
     *
     * @return true, wenn alle Einträge aus sind
     */
    private boolean checkAllDisabled()
    {
        boolean allDisabled = true;
        for (MenuEntry entry : entries)
        {
            allDisabled = allDisabled && !entry.isEnabled();
        }

        return allDisabled;
    }

    /**
     * Aufräumarbeiten.
     */
    @Override
    public void dispose() {
        Gdx.app.getApplicationLogger().log("INFO", "Menu dispose...");

        entries.clear();

        batch.dispose();

        Gdx.app.getApplicationLogger().log("INFO", "Menu leave.");
    }

    /**
     * Regiert auf Nutzereingaben.
     * Navigation und Auswahl im Menü werden hierdurch ermöglicht.
     *
     * @param keycode der Tastencode der gedrückten Taste
     * @return true, wenn auf das Ereignis reagiert wurde
     */
    @Override
    public boolean keyDown(int keycode)
    {

        InputManager.Action action = InputManager.checkMenuAction(keycode);

        if (action == InputManager.Action.MOVE_UP)
        {
            game.getAudioManager().playSound(changeSound);
            previousEntry();
            return true;
        }

        if (action == InputManager.Action.MOVE_DOWN)
        {
            game.getAudioManager().playSound(changeSound);
            nextEntry();
            return true;
        }

        if (action == InputManager.Action.INTERACTION)
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

    /**
     * Ein einfacher Menüeintrag.
     *
     * @author nico
     */
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

    /**
     * Abstandshalter für Menüs.
     *
     * @author nico
     */
    class MenuSpacer extends MenuEntry {

        public MenuSpacer(int height)
        {
            setEnabled(false);
            setCustomRendering(true);
            setHeight(height);
        }
    }

    /**
     * Nicht auswählbare Menüeinträge zum Beispiel
     * Erklärungen.
     *
     * @author nico
     */
    class MenuLabel extends MenuEntry {

        private final GlyphLayout fontLayout;
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

    /**
     * Menüüberschrift
     *
     * @author nico
     */
    class MenuTitle extends MenuEntry {

        private final GlyphLayout fontLayout;
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

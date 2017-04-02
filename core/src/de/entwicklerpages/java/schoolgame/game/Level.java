package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Iterator;

import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.common.ActionCallback;

/**
 * Basisklasse für alle Level
 * Regelt den Spielablauf
 *
 * @author nico
 */
public abstract class Level implements Disposable {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Zugriff auf die Spielinstanz
     *
     * @see SchoolGame
     */
    protected SchoolGame game;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // INTERNE EIGENSCHAFTEN

    /**
     * Zugriff auf den LevelManager
     *
     * @see LevelManager
     */
    private LevelManager levelManager;

    /**
     * DialogManager um Dialoge anzuzeigen.
     *
     * @see DialogManager
     * @see CutScene
     */
    private DialogManager dialogManager;

    /**
     * Aktueller Spielzustand
     *
     * @see LevelState
     */
    private LevelState levelState = LevelState.INTRO;

    /**
     * Die aktive CutScene
     *
     * @see CutScene
     */
    private CutScene activeCutScene;

    /**
     * Zugriff auf häufig benötigte Texte
     *
     * Wird zum Beispiel vom Ingame Menü verwendet.
     *
     * @see IngameMenu
     */
    private I18NBundle localeBundle;

    /**
     * Wird über dem Spiel eingeblendet um das Spiel zu pausieren oder zu beenden.
     */
    private IngameMenu ingameMenu;

    /**
     * Gibt dem Level überall zugriff auf die Kamerainstanz des Spieles.
     *
     * @see SchoolGame#getCamera()
     */
    private OrthographicCamera camera;

    /**
     * Speichert den Namen der Map, die zu dem entsprechendem Level gehört.
     * Wird im Konstruktor gesetzt.
     */
    private String mapName;

    /**
     * Speichert die tileMap, die für dieses Level benutzt wird.
     */
    private TiledMap tileMap;

    /**
     * Rendert die tileMap des Levels
     *
     * @see Level#tileMap
     */
    private OrthogonalTiledMapRenderer tileMapRenderer;

    /**
     * Die Breite der Map.
     *
     * @see Level#parseMap()
     */
    private float mapWidth;

    /**
     * Die Höhe der Map.
     *
     * @see Level#parseMap()
     */
    private float mapHeight;

    /**
     * Repräsentiert den Spieler, wird auch für die Kamerasteuerung verwendet.
     */
    private Player player;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BASIS METHODEN

    /**
     * Geschützter Konstruktor
     * Er zwingt die abgeleiteten Klassen dazu, einen eigenen Konstruktor zu definieren.
     * Diese können dann super("name"); aufrufen, um die Map zu laden.
     *
     * @param map Den Namen der Map, die geladen werden soll
     */
    protected Level(String map)
    {
        mapName = map;
    }

    /**
     * Wird automatisch vom LevelManager aufgerufen, wenn ein Level geladen werden soll.
     * Hier werden die Eigenschaften der Klasse initialisiert.
     *
     * @param game Zugriff auf die Spielinstanz
     * @param manager Zugriff auf den LevelManager
     * @param saveData Zugriff auf die Spielerdaten
     */
    public final void create(SchoolGame game, LevelManager manager, SaveData saveData)
    {
        Gdx.app.log("INFO", "Load level " + mapName + " ...");

        this.game = game;
        this.levelManager = manager;

        try {
            this.dialogManager = new DialogManager(game, mapName, saveData.getPlayerName());
        } catch (Exception e) {
            Gdx.app.error("ERROR", "Abort level loading!");
            levelManager.exitToMenu();
            return;
        }

        activeCutScene = this.getIntroCutScene();
        if (activeCutScene == null)
            levelState = LevelState.PLAYING;
        else
        {
            dialogManager.setFinishedCallback(new ActionCallback()
            {
                @Override
                public void run()
                {
                    levelState = LevelState.PLAYING;
                    activeCutScene = null;
                }
            });

            dialogManager.startDialog(activeCutScene.getDialogId());
        }

        camera = game.getCamera();

        player = new Player(saveData.getPlayerName(), saveData.isMale());

        localeBundle = I18NBundle.createBundle(Gdx.files.internal("I18n/Game"));

        initMap();

        ingameMenu = new IngameMenu(game, this);

        Gdx.app.log("INFO", "Level loaded.");
    }

    /**
     * Wird in jedem Frame einmal aufgerufen um Berechnungen durchzuführen.
     *
     * @param deltaTime Die Zeit, die seit dem letztem Frame vergangen ist
     */
    public final void update(float deltaTime)
    {
        if (levelState != LevelState.PLAYING) return;

        // AnimatedTiledMapTile.updateAnimationBaseTime();

        player.update(deltaTime);

        setCameraInBounds();
    }

    /**
     * Wird bei jedem Frame einmal aufgerufen um das Spiel zu zeichnen.
     *
     * @param deltaTime Die Zeit, die seit dem letztem Frame vergangen ist
     */
    public final void render(float deltaTime)
    {
        if (levelState != LevelState.INTRO && levelState != LevelState.OUTRO)
        {
            tileMapRenderer.setView(camera);
            tileMapRenderer.render();

            player.render(camera, deltaTime);
        }

        if (dialogManager.isPlaying())
            dialogManager.render(camera, deltaTime);

        if (levelState == LevelState.PAUSE)
            ingameMenu.render(camera);
    }

    /**
     * Wird aufgerufen, wenn das Level beendet werden soll.
     * Hier werden nicht mehr benötigte Resourcen freigegeben.
     */
    public final void dispose()
    {
        if (tileMapRenderer != null)
            tileMapRenderer.dispose();

        if (player != null)
            player.dispose();

        if (tileMap != null)
            tileMap.dispose();
    }

    /**
     * Wird immer aufgerufen, wenn der Spieler eine Taste runter drückt.
     *
     * @param keycode Der Tastencode der Taste, die gedrückt wurde
     * @return true wenn es eine Aktion auf dieses Event gab.
     */
    public final boolean keyDown(int keycode)
    {
        if (keycode == Input.Keys.ESCAPE)
        {
            if (levelState == LevelState.PLAYING)
            {
                ingameMenu.reset();
                setPause();
            }
            else if (levelState == LevelState.PAUSE)
            {
                setPlaying();
            }
            return true;
        }

        if (dialogManager.isPlaying())
            return  dialogManager.handleInput(keycode);

        if (levelState == LevelState.PAUSE)
            return ingameMenu.handleInput(keycode);

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ÜBERSCHREIBBARE METHODEN

    /**
     * Erlaubt einem abgeleitetem Level, eine Intro CutScene festzulegen.
     * @return Die CutScene, die angezeigt werden soll, sonst null
     */
    @SuppressWarnings("SameReturnValue")
    public CutScene getIntroCutScene()
    {
        return null;
    }

    /**
     * Erlaubt einem abgeleitetem Level, eine Outro CutScene festzulegen.
     * @return Die CutScene, die angezeigt werden soll, sonst null
     */
    @SuppressWarnings("SameReturnValue")
    public CutScene getOutroCutScene()
    {
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // AKTIONS METHODEN

    /**
     * Beendet das Level und springt zum Menü.
     * Ist nur eine Weiterleitung zum LevelManager
     *
     * @see LevelManager#exitToMenu()
     */
    public final void exitToMenu()
    {
        levelManager.exitToMenu();
    }

    /**
     * Beendet das Level und springt in die Credits.
     * Ist nur eine Weiterleitung zum LevelManager.
     * Besonders praktisch für die letzten Level eines Story-Strangs.
     *
     * @see LevelManager#exitToCredits()
     */
    public final void exitToCredits()
    {
        levelManager.exitToCredits();
    }

    /**
     * Wechselt das Level.
     * Wenn es eine OutroCutScene gibt wird diese zuvor gezeigt.
     *
     * @param newLevel Das Level, zu dem gewechselt werden soll
     *
     * @see LevelManager#changeLevel(String)
     * @see Level#getOutroCutScene()
     */
    protected final void changeLevel(final String newLevel)
    {
        activeCutScene = this.getOutroCutScene();
        if (activeCutScene == null)
            levelManager.changeLevel(newLevel);
        else {
            levelState = LevelState.OUTRO;

            dialogManager.setFinishedCallback(new ActionCallback()
            {
                @Override
                public void run()
                {
                    levelManager.changeLevel(newLevel);
                }
            });

            dialogManager.startDialog(activeCutScene.getDialogId());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTER UND SETTER

    /**
     * Gibt das allgemeine Übersetzungspaket für Level zurück.
     *
     * @return das Übersetzungspaket für Level
     */
    public final I18NBundle getLocaleBundle()
    {
        return localeBundle;
    }

    /**
     * Lässt zu, dass das Spiel auch von Außen pausiert werden kann.
     */
    public final void setPause() {
        levelState = LevelState.PAUSE;
    }

    /**
     * Lässt zu, dass das Spiel auch von Außen fortgesetzt werden kann (z.B. aus dem Ingame Menü)
     *
     * @see IngameMenu
     */
    public final void setPlaying() {
        levelState = LevelState.PLAYING;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // INTERNE METHODEN

    /**
     * Lädt die Map aus der Datei in den Speicher.
     *
     * Wird von {@link #initMap()} aufgerufen.
     */
    private void loadMap()
    {
        FileHandle mapFile = Gdx.files.internal("maps/" + mapName + ".tmx");

        if (!mapFile.exists() || mapFile.isDirectory())
        {
            Gdx.app.error("ERROR", "The map file " + mapName + ".tmx doesn't exists!");
            levelManager.exitToMenu();
            return;
        }

        TmxMapLoader.Parameters mapLoaderParameters = new TmxMapLoader.Parameters();
        mapLoaderParameters.textureMagFilter = Texture.TextureFilter.Nearest;
        mapLoaderParameters.textureMinFilter = Texture.TextureFilter.Nearest;

        tileMap = new TmxMapLoader().load(mapFile.path(), mapLoaderParameters);
    }

    /**
     * Parst die Map und sucht nach Objekten und Animationen.
     *
     * Wird von {@link #initMap()} aufgerufen.
     */
    private void parseMap()
    {
        TiledMapTileLayer firstLayer = (TiledMapTileLayer) tileMap.getLayers().get(0);

        mapWidth = firstLayer.getWidth() * firstLayer.getTileWidth();
        mapHeight = firstLayer.getHeight() * firstLayer.getTileHeight();

        Iterator<MapObject> objects = tileMap.getLayers().get(LevelConstants.TMX_OBJECT_LAYER).getObjects().iterator();
        while (objects.hasNext())
        {
            MapObject tileObject = objects.next();

            Gdx.app.log("LEVEL", "Found object '" + tileObject.getName() + "'");

            Iterator<String> props = tileObject.getProperties().getKeys();

            while (props.hasNext())
            {
                String key = props.next();
                Gdx.app.log("LEVEL", "Property: " + key + " - " + tileObject.getProperties().get(key).toString());
            }

            if (tileObject.getProperties().containsKey("type")
                    && tileObject.getProperties().get("type", String.class).equals(LevelConstants.TMX_START_POSITION))
            {
                EllipseMapObject start = (EllipseMapObject) tileObject;
                player.setPosition(start.getEllipse().x, start.getEllipse().y);
            }
        }
    }

    /**
     * Initialisiert die Map.
     * Ruft in erster Linie nur andere Methoden auf.
     *
     * @see Level#loadMap()
     * @see Level#parseMap()
     */
    private void initMap()
    {
        loadMap();
        parseMap();

        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap, 1f);
    }

    /**
     * Setzt die Kamera auf die Spielerposition.
     * Sollte die Kamera den Rand der Karte erreichen, wird sie gestoppt.
     */
    private void setCameraInBounds()
    {
        float cameraHalfWidth = camera.viewportWidth * .5f;
        float cameraHalfHeight = camera.viewportHeight * .5f;

        float camX = player.getPosX();
        float camY = player.getPosY();

        if (cameraHalfWidth < mapWidth - cameraHalfWidth)
            camX = MathUtils.clamp(camX, cameraHalfWidth, mapWidth - cameraHalfWidth);
        else
            camX = mapWidth / 2;

        if (cameraHalfHeight < mapHeight - cameraHalfHeight)
            camY = MathUtils.clamp(camY, cameraHalfHeight, mapHeight - cameraHalfHeight);
        else
            camY = mapHeight / 2;

        camera.position.set(camX, camY, 0);
        camera.update();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ABSTRAKTE METHODEN

    /**
     * Gibt den Titel des Levels zurück.
     *
     * @return Der Titel des Levels
     */
    public abstract String getTitle();

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ENDE

    /**
     * Definiert die möglichen Zustände eines Levels.
     *
     * @author nico
     */
    private enum LevelState
    {
        INTRO,
        PLAYING,
        PAUSE,
        OUTRO
    }

    private final class LevelConstants
    {
        static final String TMX_OBJECT_LAYER = "Objekte";
        static final String TMX_START_POSITION = "Start";

        private LevelConstants() {}
    }
}

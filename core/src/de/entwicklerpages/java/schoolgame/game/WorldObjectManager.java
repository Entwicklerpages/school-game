package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.game.objects.WorldObject;

/**
 * Verwaltet alle Ingame Objekte wie
 * <ul>
 *     <li>Gegner</li>
 *     <li>Items</li>
 *     <li>Trigger Zonen</li>
 *     <li>statische Objekte (Türen, NPCs)</li>
 * </ul>
 *
 * @author nico
 */
public class WorldObjectManager
{
    private WorldObjectConfig config;
    private List<WorldObject> worldObjects;
    private final SchoolGame game;
    private final World physicalWorld;

    /**
     * Erstellt einen neuen WorldObjectManager.
     *
     * @param game Zugriff auf die Spielinstanz
     * @param physicalWorld Zugriff auf die Box2D Welt
     */
    public WorldObjectManager(SchoolGame game, World physicalWorld)
    {
        this.game = game;
        this.physicalWorld = physicalWorld;

        worldObjects = new ArrayList<WorldObject>();
    }

    /**
     * Initialisiert ein Tiled MapObject.
     *
     * @param objectId die ID des Objektes
     * @param object Zugriff auf das Objekt selbst
     */
    public void initObject(String objectId, MapObject object)
    {
        if (config == null)
        {
            Gdx.app.error("ERROR", "No world config was created!");
            return;
        }

        for (WorldObject worldObject : config.getWorldObjects())
        {
            if (worldObject.getObjectId().equals(objectId))
            {
                worldObject.setWorldObjectManager(this);
                worldObject.setRawObject(object);
                worldObject.onInit();
                worldObjects.add(worldObject);
                config.getWorldObjects().remove(worldObject);
                return;
            }
        }

        Gdx.app.log("WARNING", "Object '" + objectId + "' is not a part of the level!");
    }

    /**
     * Sperrt die Konfiguration.
     */
    public void lockConfig()
    {
        config.lockConfig();
    }

    /**
     * Schließt die Initialisierung ab.
     * Sollten Objekte nicht in der TiledMap gefunden worden sein, wird eine Warnung ausgegeben.
     */
    public void finishInit()
    {
        if (config == null)
        {
            Gdx.app.error("ERROR", "No world config was created!");
            return;
        }

        if (!config.getWorldObjects().isEmpty())
        {
            for (WorldObject object : config.getWorldObjects())
            {
                Gdx.app.log("WARNING", "Object '" + object.getObjectId() + "' was not found in the map!");
            }
        }

        Gdx.app.log("INFO", "World is ready.");
    }

    /**
     * Erzeugt eine neue Konfiguration.
     *
     * @see WorldObjectConfig
     *
     * @return die leere Konfiguration
     */
    public WorldObjectConfig createConfig()
    {
        if (config == null)
        {
            config = new WorldObjectConfig();
        }
        return config;
    }

    /**
     * Reagiert auf Spielerinteraktionen.
     *
     * @param player Zugriff auf die Spielerinstanz (Position)
     */
    public void playerInteraction(Player player)
    {

    }

    /**
     * Erlaubt vorallem Welt Objekten den Zugriff auf die Spielinstanz.
     *
     * @return die Spielinstanz
     */
    public SchoolGame getGame()
    {
        return game;
    }

    /**
     * Erlaubt vorallem Welt Objekten den Zugriff auf die Box2D Welt.
     *
     * @return die Box2D Welt
     */
    public World getPhysicalWorld()
    {
        return physicalWorld;
    }

    /**
     * Speichert die Liste der Welt Objekte, die in der TiledMap vorkommen sollten.
     *
     * @author nico
     */
    public class WorldObjectConfig
    {
        private List<WorldObject> worldObjects = new ArrayList<WorldObject>();
        private boolean locked;

        /**
         * Privater Konstruktor.
         * Nur der WorldObjectManager darf Konfigurationen erstellen.
         *
         * @see WorldObjectManager#createConfig()
         */
        private WorldObjectConfig()
        {
            locked = false;
        }

        /**
         * Ruft die Liste der Weltobjekte ab.
         * Zugriff nur für den WorldObjectManager.
         *
         * @return die Liste der Weltobjekte
         */
        private List<WorldObject> getWorldObjects()
        {
            return worldObjects;
        }

        /**
         * Sperrt die Konfiguration.
         * Nach dem Aufruf können keine Objekte mehr hinzugefügt werden.
         */
        private void lockConfig()
        {
            locked = true;
        }

        /**
         * Fügt ein Objekt zur Welt hinzu.
         * Es muss auch in der TiledMap existieren.
         *
         * @param worldObject das neue Objekt
         */
        public void registerObject(WorldObject worldObject)
        {
            if (locked)
            {
                Gdx.app.error("ERROR", "Tried to add object after the config was locked!");
                return;
            }

            worldObjects.add(worldObject);
        }
    }
}

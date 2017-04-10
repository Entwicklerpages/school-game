package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.*;
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
    private SchoolGame game;
    private World physicalWorld;

    public WorldObjectManager(SchoolGame game, World physicalWorld)
    {
        this.game = game;
        this.physicalWorld = physicalWorld;

        worldObjects = new ArrayList<WorldObject>();
    }


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

    public void lockConfig()
    {
        config.lockConfig();
    }

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

    public WorldObjectConfig createConfig()
    {
        if (config == null)
        {
            config = new WorldObjectConfig();
        }
        return config;
    }

    public SchoolGame getGame()
    {
        return game;
    }

    public World getPhysicalWorld()
    {
        return physicalWorld;
    }

    public class WorldObjectConfig
    {
        private List<WorldObject> worldObjects = new ArrayList<WorldObject>();
        private boolean locked;

        private WorldObjectConfig()
        {
            locked = false;
        }

        private List<WorldObject> getWorldObjects()
        {
            return worldObjects;
        }

        private void lockConfig()
        {
            locked = true;
        }

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

package de.entwicklerpages.java.schoolgame.game.objects;

import com.badlogic.gdx.maps.MapObject;

import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Basisklasse für Weltobjekte
 *
 * @author nico
 */
public abstract class WorldObject
{
    protected final String objectId;
    protected MapObject rawObject;
    protected WorldObjectManager worldObjectManager;

    public WorldObject(String objectId)
    {
        this.objectId = objectId;
    }

    public void onInit()
    {
    }

    public void onAttack()
    {
    }

    public void onInteract()
    {
    }

    public final String getObjectId()
    {
        return objectId;
    }

    public final MapObject getRawObject()
    {
        return rawObject;
    }

    public final void setRawObject(MapObject rawObject)
    {
        this.rawObject = rawObject;
    }

    public final void setWorldObjectManager(WorldObjectManager worldObjectManager)
    {
        this.worldObjectManager = worldObjectManager;
    }
}

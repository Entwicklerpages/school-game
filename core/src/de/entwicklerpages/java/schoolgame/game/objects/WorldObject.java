package de.entwicklerpages.java.schoolgame.game.objects;

import com.badlogic.gdx.maps.MapObject;

/**
 * Basisklasse für Weltobjekte
 *
 * @author nico
 */
public abstract class WorldObject
{
    protected String objectId;
    protected MapObject rawObject;

    public WorldObject(String objectId)
    {
        this.objectId = objectId;
    }

    public void onAttack()
    {
    }

    public void onInteract()
    {
    }

    public String getObjectId()
    {
        return objectId;
    }

    public MapObject getRawObject()
    {
        return rawObject;
    }

    public void setRawObject(MapObject rawObject)
    {
        this.rawObject = rawObject;
    }
}

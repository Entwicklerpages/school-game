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

    /**
     * Konstruktor.
     *
     * @param objectId die ID des Objektes
     */
    public WorldObject(String objectId)
    {
        this.objectId = objectId;
    }

    /**
     * Wird aufgerufen wenn das Objekt in der Map gefunden wurde.
     *
     * Macht selbst nichts, kann aber überschrieben werden.
     */
    public void onInit()
    {
    }

    /**
     * Gibt die Objekt ID zurück.
     *
     * @return die Objekt ID
     */
    public final String getObjectId()
    {
        return objectId;
    }

    /**
     * Gibt das MapObject zurück, das dieses Objekt in der TiledMap repräsentiert.
     *
     * @return das MapObject
     */
    public final MapObject getRawObject()
    {
        return rawObject;
    }

    /**
     * Setzt das MapObject, das dieses Objekt in der TiledMap repräsentiert.
     *
     * @param rawObject das MapObject
     */
    public final void setRawObject(MapObject rawObject)
    {
        this.rawObject = rawObject;
    }

    /**
     * Setzt denn WorldObjectManager, zu dem dieses Objekt gehört.
     * Wird vor onInit aufgerufen.
     *
     * @see WorldObject#onInit()
     *
     * @param worldObjectManager der Manager dieses Objektes
     */
    public final void setWorldObjectManager(WorldObjectManager worldObjectManager)
    {
        this.worldObjectManager = worldObjectManager;
    }
}

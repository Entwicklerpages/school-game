package de.entwicklerpages.java.schoolgame.game.objects.entities;

import de.entwicklerpages.java.schoolgame.game.objects.WorldObject;

/**
 * Basisklasse für "lebende" Objekte
 *
 * @author nico
 */
public abstract class BaseEntity extends WorldObject
{
    public BaseEntity(String objectId)
    {
        super(objectId);
    }
}

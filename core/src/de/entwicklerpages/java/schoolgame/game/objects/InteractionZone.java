package de.entwicklerpages.java.schoolgame.game.objects;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;

/**
 * Interaktionszone.
 *
 * Statische, generische Interaktionszone.
 *
 * @see InteractionHandler
 *
 * @author nico
 */
public class InteractionZone extends WorldObject
{
    private int priority;
    private InteractionHandler handler;
    private ActionCallback actionCallback = null;
    private InteractionHandler.InteractionCallback interactionCallback = null;

    /**
     * Konstruktor.
     *
     * @param objectId die ID des MapObjects
     */
    public InteractionZone(String objectId)
    {
        this(objectId, 0);
    }

    /**
     * Konstruktor.
     *
     * @param objectId die ID des MapObjects
     * @param priority die Priorität dieser Zone
     */
    public InteractionZone(String objectId, int priority)
    {
        super(objectId);

        this.priority = priority;
    }

    /**
     * Initialisierung
     */
    @Override
    public void onInit()
    {
        handler = new InteractionHandler(worldObjectManager, priority);
        handler.createStaticInteractionBody(rawObject);
        handler.setInteractionCallback(interactionCallback);
        handler.setActionCallback(actionCallback);
    }

    /**
     * Legt die Priorität fest.
     *
     * @param priority die Priorität dieser Zone
     */
    public void setPriority(int priority)
    {
        if (handler != null)
            handler.setPriority(priority);
        else
            this.priority = priority;
    }

    /**
     * Legt ein Callback fest. Dieses Callback bekommt bei einer Interkation Zugriff auf die Spielerinstanz.
     *
     * @param interactionCallback das Callback
     */
    public void setInteractionCallback(InteractionHandler.InteractionCallback interactionCallback)
    {
        if (handler != null)
            handler.setInteractionCallback(interactionCallback);
        else
            this.interactionCallback = interactionCallback;
    }

    /**
     * Legt ein Callback fest. Dieses Callback bekommt bei einer Interkation KEINEN Zugriff auf die Spielerinstanz.
     *
     * @param actionCallback das Callback
     */
    public void setActionCallback(ActionCallback actionCallback)
    {
        if (handler != null)
            handler.setActionCallback(actionCallback);
        else
            this.actionCallback = actionCallback;
    }

    @Override
    public void onDispose()
    {
        handler.dispose();
    }
}

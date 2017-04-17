package de.entwicklerpages.java.schoolgame.game.objects;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;

/**
 * @author nico
 */
public class InteractionZone extends WorldObject
{
    private int priority;
    private InteractionHandler handler;
    private ActionCallback actionCallback = null;
    private InteractionHandler.InteractionCallback interactionCallback = null;

    public InteractionZone(String objectId)
    {
        this(objectId, 0);
    }

    public InteractionZone(String objectId, int priority)
    {
        super(objectId);

        this.priority = priority;
    }

    @Override
    public void onInit()
    {
        handler = new InteractionHandler(worldObjectManager, priority);
        handler.createStaticInteractionBody(rawObject);
        handler.setInteractionCallback(interactionCallback);
        handler.setActionCallback(actionCallback);
    }

    public void setPriority(int priority)
    {
        if (handler != null)
            handler.setPriority(priority);
        else
            this.priority = priority;
    }

    public void setInteractionCallback(InteractionHandler.InteractionCallback interactionCallback)
    {
        if (handler != null)
            handler.setInteractionCallback(interactionCallback);
        else
            this.interactionCallback = interactionCallback;
    }

    public void setActionCallback(ActionCallback actionCallback)
    {
        if (handler != null)
            handler.setActionCallback(actionCallback);
        else
            this.actionCallback = actionCallback;
    }
}

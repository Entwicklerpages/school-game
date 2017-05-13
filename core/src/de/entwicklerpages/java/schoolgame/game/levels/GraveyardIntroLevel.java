package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;

/**
 * Erstes Friedhof Level
 *
 * LEVEL INFO
 * -----
 * Einführung in den Friedhof
 *
 * @author nico
 */
@SuppressWarnings("unused") // Dise Klasse wird über Reflection geladen
public class GraveyardIntroLevel extends Level
{
    public static final String LEVEL_NAME = "graveyard_intro";

    public GraveyardIntroLevel() {
        super(LEVEL_NAME);
    }

    private boolean talkedToScull = false;

    @Override
    public String getTitle() {
        return "Friedhof: Einführung";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        InteractionZone signInteraction = new InteractionZone("Schild");
        signInteraction.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schild");
            }
        });

        Trigger scullTrigger = new Trigger("Schaedel");
        scullTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                if (!talkedToScull)
                {
                    startDialog("schaedel");
                    talkedToScull = true;
                } else {
                    startDialog("schaedel2");
                }
            }
        });

        for (int i = 1; i <= 15; i++)
        {
            worldConfig.registerObject(createGrave(i));
        }

        Trigger exitTrigger = new Trigger("Ausgang");
        scullTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                changeLevel("graveyard_battle");
            }
        });

        worldConfig.registerObject(signInteraction);
        worldConfig.registerObject(scullTrigger);
    }

    private InteractionZone createGrave(final int id)
    {
        InteractionZone graveInteraction = new InteractionZone("Grab" + id);
        graveInteraction.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("grab" + id);
            }
        });

        return graveInteraction;
    }
}

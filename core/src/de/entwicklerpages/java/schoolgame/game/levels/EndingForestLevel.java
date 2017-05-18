package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;
import de.entwicklerpages.java.schoolgame.game.objects.entities.npc.EndingWolf;

/**
 * Ende pro-Wald Level
 *
 * LEVEL INFO
 * -----
 * Story - Krieger ist besiegt.
 *
 * @author nico
 */
public class EndingForestLevel extends Level
{
    public static final String LEVEL_NAME = "ending_forest";

    public EndingForestLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Ende: Wald";
    }

    @Override
    protected String getMusicName()
    {
        return "skye_cuillin";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        InteractionZone witch = new InteractionZone("Heilerin");
        witch.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("heilerin");
            }
        });

        Trigger wolfInteraction = new Trigger("Wolf_interaktion");
        wolfInteraction.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("wolf", new ActionCallback()
                {
                    @Override
                    public void run()
                    {
                        exitToCredits();
                    }
                });
            }
        });

        EndingWolf wolf = new EndingWolf("Wolf");

        worldConfig.registerObject(witch);
        worldConfig.registerObject(wolfInteraction);
        worldConfig.registerObject(wolf);
    }
}

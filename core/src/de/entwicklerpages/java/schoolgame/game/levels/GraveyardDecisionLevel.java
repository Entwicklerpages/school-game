package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.CutScene;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;
import de.entwicklerpages.java.schoolgame.game.objects.entities.Crystal;

/**
 * Viertes Friedhof Level
 *
 * LEVEL INFO
 * -----
 * Story
 *
 * @author nico
 */
public class GraveyardDecisionLevel extends Level
{
    public static final String LEVEL_NAME = "graveyard_decision";

    public GraveyardDecisionLevel() {
        super(LEVEL_NAME);
    }

    private boolean voices[] = new boolean[5];

    private boolean crystalTaken = false;

    @Override
    public String getTitle() {
        return "Friedhof: Entscheidung";
    }

    @Override
    protected CutScene getOutroCutScene()
    {
        return crystalTaken ? new CutScene(null, "outro") : null;
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        for (int i = 1; i <= 5; i++)
        {
            worldConfig.registerObject(createVoice(i));
        }

        InteractionZone crystalInteraction = new InteractionZone("Kristall_interaktion");
        crystalInteraction.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                crystalTaken = true;
                changeLevel("graveyard_boss_dragon");
            }
        });

        Crystal crystal = new Crystal("Kristall");

        Trigger exitTrigger = new Trigger("Ausgang");
        exitTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                if (voices[3])
                    changeLevel("graveyard_boss_warrior");
            }
        });

        worldConfig.registerObject(crystalInteraction);
        worldConfig.registerObject(crystal);
        worldConfig.registerObject(exitTrigger);
    }

    private Trigger createVoice(final int id)
    {
        Trigger graveInteraction = new Trigger("Stimme" + id);
        graveInteraction.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                if (!voices[id - 1])
                {
                    startDialog("stimme" + id);
                    voices[id - 1] = true;
                }
            }
        });

        return graveInteraction;
    }
}

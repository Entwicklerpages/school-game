package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.CutScene;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;
import de.entwicklerpages.java.schoolgame.game.objects.entities.Crystal;
import de.entwicklerpages.java.schoolgame.game.objects.entities.StoneBarrier;
import de.entwicklerpages.java.schoolgame.game.objects.entities.enemy.Crab;

/**
 * Drittes Höhlen Level
 *
 * LEVEL INFO
 * Bosskampf
 *
 * @author nico
 */
public class CaveBossLevel extends Level
{
    public static final String LEVEL_NAME = "cave_boss";

    private boolean battle = false;
    private int crabCount = 12;

    private StoneBarrier barrier1;

    public CaveBossLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Höhle: Bosskampf";
    }

    @Override
    protected String getMusicName()
    {
        return "future_gladiator";
    }

    @Override
    protected CutScene getOutroCutScene()
    {
        return new CutScene(null, "stimme");
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        barrier1 = new StoneBarrier("Stein1");
        worldConfig.registerObject(barrier1);

        final StoneBarrier barrier2 = new StoneBarrier("Stein2", false);
        worldConfig.registerObject(barrier2);

        ActionCallback startBattle = new ActionCallback()
        {
            @Override
            public void run()
            {
                if (!battle)
                {
                    barrier2.create();
                    battle = true;
                }
            }
        };

        for (int i = 1; i <= crabCount; i++)
        {
            worldConfig.registerObject(createCrab(i));
        }

        Trigger trigger1 = new Trigger("Trigger_start1", startBattle);
        worldConfig.registerObject(trigger1);

        Trigger trigger2 = new Trigger("Trigger_start2", startBattle);
        worldConfig.registerObject(trigger2);

        Trigger trigger3 = new Trigger("Trigger_start3", startBattle);
        worldConfig.registerObject(trigger3);

        InteractionZone crystalInteraction = new InteractionZone("Kristall_interaktion");
        crystalInteraction.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                changeLevel("cave_village");
            }
        });
        worldConfig.registerObject(crystalInteraction);

        Crystal crystal = new Crystal("Kristall");
        worldConfig.registerObject(crystal);
    }

    private Crab createCrab(final int id)
    {
        Crab crab = new Crab("Krabbe" + id);
        crab.setDeathCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                crabCount--;

                if (crabCount <= 0 && barrier1 != null)
                {
                    barrier1.destroy();
                    barrier1 = null;
                }
            }
        });

        return crab;
    }
}

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
import de.entwicklerpages.java.schoolgame.game.objects.entities.enemy.Wolf;

/**
 * Drittes Wald Level
 *
 * LEVEL INFO
 * Bosskampf.
 *
 * @author nico
 */
public class ForestBossLevel extends Level
{
    public static final String LEVEL_NAME = "forest_boss";

    private boolean battle = false;
    private int wolfCount = 5;

    private StoneBarrier barrier_exit;

    public ForestBossLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Wald: Bosskampf";
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
        barrier_exit = new StoneBarrier("Stein_ausgang");
        worldConfig.registerObject(barrier_exit);

        final StoneBarrier barrier1 = new StoneBarrier("Stein1", false);
        worldConfig.registerObject(barrier1);

        final StoneBarrier barrier2 = new StoneBarrier("Stein2", false);
        worldConfig.registerObject(barrier2);

        final StoneBarrier barrier3 = new StoneBarrier("Stein3", false);
        worldConfig.registerObject(barrier3);

        ActionCallback startBattle = new ActionCallback()
        {
            @Override
            public void run()
            {
                if (!battle)
                {
                    barrier1.create();
                    barrier2.create();
                    barrier3.create();
                    battle = true;
                }
            }
        };

        for (int i = 1; i <= wolfCount; i++)
        {
            worldConfig.registerObject(createWolf(i));
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
                changeLevel("forest_village");
            }
        });
        worldConfig.registerObject(crystalInteraction);

        Crystal crystal = new Crystal("Kristall");
        worldConfig.registerObject(crystal);
    }

    private Wolf createWolf(final int id)
    {
        Wolf wolf = new Wolf("Wolf" + id);
        wolf.setDeathCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                wolfCount--;

                if (wolfCount <= 0 && barrier_exit != null)
                {
                    barrier_exit.destroy();
                    barrier_exit = null;
                }
            }
        });

        return wolf;
    }
}

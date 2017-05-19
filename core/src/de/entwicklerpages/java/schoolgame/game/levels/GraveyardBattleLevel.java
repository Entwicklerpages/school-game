package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;
import de.entwicklerpages.java.schoolgame.game.objects.entities.StoneBarrier;
import de.entwicklerpages.java.schoolgame.game.objects.entities.enemy.Rabbit;
import de.entwicklerpages.java.schoolgame.game.objects.entities.enemy.Skeleton;

/**
 * Zweites Friedhof Level
 *
 * LEVEL INFO
 * -----
 * Keine Story, nur Kampf!
 *
 * @author nico
 */
public class GraveyardBattleLevel extends Level
{
    public static final String LEVEL_NAME = "graveyard_battle";

    private StoneBarrier barrier;

    private int skeletonCount = 12;

    public GraveyardBattleLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Friedhof: Kampf";
    }

    @Override
    protected String getMusicName()
    {
        return "myst_on_the_moor";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        barrier = new StoneBarrier("Stein1");
        worldConfig.registerObject(barrier);

        for (int i = 1; i <= skeletonCount; i++)
        {
            worldConfig.registerObject(createSkeleton(i));
        }

        Trigger exitTrigger = new Trigger("Ausgang");
        exitTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                changeLevel("graveyard_decision");
            }
        });
        worldConfig.registerObject(exitTrigger);
    }

    private Skeleton createSkeleton(final int id)
    {
        final Skeleton skeleton = new Skeleton("Skelett_" + id);
        skeleton.setDeathCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                skeletonCount--;

                if (skeletonCount <= 0 && barrier != null)
                {
                    barrier.destroy();
                    barrier = null;
                }
            }
        });
        return skeleton;
    }
}

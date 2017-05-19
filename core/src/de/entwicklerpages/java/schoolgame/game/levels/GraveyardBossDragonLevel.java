package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.CutScene;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;
import de.entwicklerpages.java.schoolgame.game.objects.entities.StoneBarrier;
import de.entwicklerpages.java.schoolgame.game.objects.entities.enemy.Dragon;
import de.entwicklerpages.java.schoolgame.game.objects.entities.enemy.Warrior;

/**
 * Fünftes Friedhof Level
 *
 * LEVEL INFO
 * -----
 * Story - Endkampf Drache
 *
 * @author nico
 */
public class GraveyardBossDragonLevel extends Level
{
    public static final String LEVEL_NAME = "graveyard_boss_dragon";

    private boolean battle = false;

    public GraveyardBossDragonLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Friedhof: Bosskampf";
    }

    @Override
    protected CutScene getOutroCutScene()
    {
        return new CutScene(null, "drache");
    }

    @Override
    protected String getMusicName()
    {
        return "netherworld_shanty";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        final StoneBarrier barrier1 = new StoneBarrier("Stein1", false);
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
                    barrier1.create();
                    barrier2.create();
                    battle = true;
                }
            }
        };

        Dragon dragon = new Dragon("Drache");
        dragon.setDeathCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                changeLevel("ending_village");
            }
        });
        worldConfig.registerObject(dragon);

        Trigger trigger1 = new Trigger("Trigger_start1", startBattle);
        worldConfig.registerObject(trigger1);

        Trigger trigger2 = new Trigger("Trigger_start2", startBattle);
        worldConfig.registerObject(trigger2);

        Trigger trigger3 = new Trigger("Trigger_start3", startBattle);
        worldConfig.registerObject(trigger3);
    }
}

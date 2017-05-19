package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.AttackZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;
import de.entwicklerpages.java.schoolgame.game.objects.entities.enemy.Rabbit;

/**
 * Zweites Wald Level
 *
 * LEVEL INFO
 * -----
 * Keine Story, nur Kampf!
 *
 * @author nico
 */
public class ForestBattle extends Level
{
    public static final String LEVEL_NAME = "forest_battle";

    public ForestBattle() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Wald: Kampf";
    }

    @Override
    protected String getMusicName()
    {
        return "skye_cuillin";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        for (int i = 1; i <= 53; i++)
        {
            worldConfig.registerObject(createThorns(i));
        }

        for (int j = 5; j <= 12; j++)
        {
            worldConfig.registerObject(createRabbit(j));
        }

        Trigger exitTrigger = new Trigger("Ausgang");
        exitTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                changeLevel("forest_boss");
            }
        });
        worldConfig.registerObject(exitTrigger);
    }

    private AttackZone createThorns(final int id)
    {
        AttackZone throns = new AttackZone("Dornen" + id, 8);
        throns.setTimeout(2f);

        return throns;
    }

    private Rabbit createRabbit(final int id)
    {
        return new Rabbit("Rabbit_" + id);
    }
}

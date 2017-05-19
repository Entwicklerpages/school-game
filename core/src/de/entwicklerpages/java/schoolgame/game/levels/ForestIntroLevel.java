package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;

/**
 * Erstes Wald Level
 *
 * LEVEL INFO
 * Zum ersten mal im Wald.
 *
 * @author nico
 */
public class ForestIntroLevel extends Level
{
    public static final String LEVEL_NAME = "forest_intro";

    public ForestIntroLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Wald: Einführung";
    }

    @Override
    protected String getMusicName()
    {
        return "skye_cuillin";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        InteractionZone mushroom1 = new InteractionZone("Hilfe_Pilz1");
        mushroom1.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("pilz1");
            }
        });

        InteractionZone mushroom2 = new InteractionZone("Hilfe_Pilz2");
        mushroom2.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("pilz2");
            }
        });

        for (int i = 1; i <= 5; i++)
        {
            worldConfig.registerObject(createSign(i));
        }

        final Trigger exitTrigger = new Trigger("Ausgang");
        exitTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                changeLevel("forest_battle");
            }
        });

        worldConfig.registerObject(exitTrigger);
        worldConfig.registerObject(mushroom1);
        worldConfig.registerObject(mushroom2);
    }

    private InteractionZone createSign(final int id)
    {
        InteractionZone signInteraction = new InteractionZone("Schild" + id);
        signInteraction.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schild" + id);
            }
        });

        return signInteraction;
    }
}

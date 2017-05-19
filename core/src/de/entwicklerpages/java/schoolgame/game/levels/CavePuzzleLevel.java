package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.AttackZone;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;

/**
 * Zweites Höhlen Level
 *
 * LEVEL INFO
 * Rätsellevel
 *
 * @author nico
 */
public class CavePuzzleLevel extends Level
{
    public static final String LEVEL_NAME = "cave_puzzle";

    public CavePuzzleLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Höhle: Rätsel";
    }

    @Override
    protected String getMusicName()
    {
        return "the_pyre";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        for (int i = 1; i <= 6; i++)
        {
            worldConfig.registerObject(createSign(i));
        }

        InteractionZone lifeSign = new InteractionZone("LebenSchild");
        lifeSign.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("leben_schild");
            }
        });

        InteractionZone sign = new InteractionZone("Schild");
        sign.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schild");
            }
        });

        AttackZone urchin = new AttackZone("Seeigel", 7);
        urchin.setTimeout(1.27f);

        Trigger exitTrigger = new Trigger("Ausgang");
        exitTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                changeLevel("cave_boss");
            }
        });

        worldConfig.registerObject(lifeSign);
        worldConfig.registerObject(sign);
        worldConfig.registerObject(urchin);
        worldConfig.registerObject(exitTrigger);
    }

    private InteractionZone createSign(final int id)
    {
        InteractionZone signInteraction = new InteractionZone("TotSchild" + id);
        signInteraction.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("tot_schild");
            }
        });

        return signInteraction;
    }
}

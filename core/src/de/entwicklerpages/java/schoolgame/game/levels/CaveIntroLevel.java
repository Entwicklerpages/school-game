package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.CutScene;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.AttackZone;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;

/**
 * Erstes Höhlen Level
 *
 * LEVEL INFO
 * Zum ersten mal in der Höhle.
 *
 * @author nico
 */
public class CaveIntroLevel extends Level
{
    public static final String LEVEL_NAME = "cave_intro";

    public CaveIntroLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Höhle: Einführung";
    }

    @Override
    protected CutScene getIntroCutScene()
    {
        return new CutScene(null, "intro");
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        InteractionZone sign = new InteractionZone("Schild");
        sign.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schild");
            }
        });


        for (int i = 1; i <= 24; i++)
        {
            worldConfig.registerObject(createUrchin(i));
        }

        final Trigger exitTrigger = new Trigger("Ausgang");
        exitTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                changeLevel("cave_puzzle");
            }
        });

        worldConfig.registerObject(exitTrigger);
        worldConfig.registerObject(sign);
    }

    private AttackZone createUrchin(final int id)
    {
        AttackZone urchinTrigger = new AttackZone("Seeigel" + id, 10);
        urchinTrigger.setTimeout(1.5f);

        return urchinTrigger;
    }
}

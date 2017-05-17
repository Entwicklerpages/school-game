package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;

/**
 * Höhlen Level
 *
 * LEVEL INFO
 * Hauptkreuzung: Weg zum Wald
 *
 * @author nico
 */
public class CaveCrossingLevel extends Level
{
    public static final String LEVEL_NAME = "cave_crossing";

    public CaveCrossingLevel() {
        super(LEVEL_NAME);
    }

    private boolean talkedToKids = false;

    @Override
    public String getTitle() {
        return "Höhle: Kreuzung";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        InteractionZone sign_direction = new InteractionZone("Schild Wegweiser");
        sign_direction.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("wegweiser");
            }
        });

        InteractionZone sign_cave = new InteractionZone("Schild Hoehle", 2);
        sign_cave.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("hoehle");
            }
        });

        InteractionZone sign_forest = new InteractionZone("Schild Wald", 2);
        sign_forest.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("wald");
            }
        });

        InteractionZone barrierInfo = new InteractionZone("Sperre Info");
        barrierInfo.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("sperre");
            }
        });

        InteractionZone diary = new InteractionZone("Abenteuer Text");
        diary.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("tagebuch2");
            }
        });

        InteractionZone barrierCave = new InteractionZone("Hoehle Sperre");
        barrierCave.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("neue_sperre");
            }
        });

        final InteractionZone kids = new InteractionZone("Kinder");
        kids.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                if (!talkedToKids)
                {
                    startDialog("kinder");
                    talkedToKids = true;
                } else {
                    startDialog("kinder_fertig");
                }
            }
        });

        final Trigger exitTrigger = new Trigger("Ausgang");
        exitTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                changeLevel("cave_intro");
            }
        });

        worldConfig.registerObject(sign_direction);
        worldConfig.registerObject(sign_cave);
        worldConfig.registerObject(sign_forest);
        worldConfig.registerObject(barrierInfo);
        worldConfig.registerObject(diary);
        worldConfig.registerObject(barrierCave);
        worldConfig.registerObject(kids);
        worldConfig.registerObject(exitTrigger);
    }
}

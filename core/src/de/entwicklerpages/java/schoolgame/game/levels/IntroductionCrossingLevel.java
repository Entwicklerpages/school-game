package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;

/**
 * Einstiegs Level
 *
 * LEVEL INFO
 * Hauptkreuzung: Weg zur Höhle
 *
 * @author nico
 */
public class IntroductionCrossingLevel extends Level
{
    public static final String LEVEL_NAME = "introduction_crossing";

    public IntroductionCrossingLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Einführung: Kreuzung";
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

        InteractionZone sign_cave = new InteractionZone("Schild Hoehle");
        sign_cave.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("hoehle");
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
                startDialog("tagebuch1");
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
        worldConfig.registerObject(barrierInfo);
        worldConfig.registerObject(diary);
        worldConfig.registerObject(exitTrigger);
    }
}

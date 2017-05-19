package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;

/**
 * Nach dem Wald zurück im Dorf
 *
 * LEVEL INFO
 * Neue Aufgabe.
 *
 * @author nico
 */
public class ForestCrossingLevel extends Level
{
    public static final String LEVEL_NAME = "forest_crossing";

    public ForestCrossingLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Wald: Kreuzung";
    }

    @Override
    protected String getMusicName()
    {
        return "birds";
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

        InteractionZone sign_graveyard = new InteractionZone("Schild Friedhof", 2);
        sign_graveyard.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("friedhof");
            }
        });

        InteractionZone barrierForest = new InteractionZone("Wald Sperre");
        barrierForest.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("neue_sperre");
            }
        });

        InteractionZone diary = new InteractionZone("Abenteuer Text");
        diary.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("tagebuch3");
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

        InteractionZone personWarrior = new InteractionZone("Krieger");
        personWarrior.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("krieger");
            }
        });

        InteractionZone personWitch = new InteractionZone("Heilerin");
        personWitch.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("heilerin");
            }
        });

        InteractionZone personSmith = new InteractionZone("Schmied");
        personSmith.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schmied");
            }
        });

        InteractionZone personMayor = new InteractionZone("Buergermeister");
        personMayor.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("buergermeister");
            }
        });

        InteractionZone grave = new InteractionZone("Grab");
        grave.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("grab");
            }
        });

        InteractionZone sign_advertisement1 = new InteractionZone("Schild Anzeige 1");
        sign_advertisement1.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("anzeige1");
            }
        });

        InteractionZone sign_advertisement2 = new InteractionZone("Schild Anzeige 2");
        sign_advertisement2.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("anzeige2");
            }
        });

        final Trigger exitTrigger = new Trigger("Ausgang");
        exitTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                changeLevel("graveyard_intro");
            }
        });

        worldConfig.registerObject(sign_direction);
        worldConfig.registerObject(sign_cave);
        worldConfig.registerObject(sign_forest);
        worldConfig.registerObject(sign_graveyard);
        worldConfig.registerObject(barrierForest);
        worldConfig.registerObject(diary);
        worldConfig.registerObject(barrierCave);
        worldConfig.registerObject(personWarrior);
        worldConfig.registerObject(personWitch);
        worldConfig.registerObject(personSmith);
        worldConfig.registerObject(personMayor);
        worldConfig.registerObject(grave);
        worldConfig.registerObject(sign_advertisement1);
        worldConfig.registerObject(sign_advertisement2);
        worldConfig.registerObject(exitTrigger);
    }
}

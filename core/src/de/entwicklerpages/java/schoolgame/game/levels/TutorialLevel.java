package de.entwicklerpages.java.schoolgame.game.levels;

import com.badlogic.gdx.utils.Timer;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.CutScene;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;
import de.entwicklerpages.java.schoolgame.game.objects.entities.StoneBarrier;
import de.entwicklerpages.java.schoolgame.game.objects.entities.enemy.TutorialDummy;

/**
 * Tutorial Level
 *
 * LEVEL INFO
 * Bietet dem Spieler eine Anleitung zum Spiel.
 * Trägt nicht zur Story bei.
 *
 * @author nico
 */
@SuppressWarnings("unused") // Dise Klasse wird über Reflection geladen
public class TutorialLevel extends Level {
    public static final String LEVEL_NAME = "tutorial";

    private boolean movement = false;
    private boolean interaction = false;
    private boolean interaction2 = false;
    private boolean battle = false;
    private boolean battle2 = false;

    private int chickenCount = 4;

    public TutorialLevel() {
        super("tutorial");
    }

    @Override
    public String getTitle() {
        return "Tutorial";
    }

    @Override
    public CutScene getIntroCutScene()
    {
        return new CutScene(null, "intro");
    }

    @Override
    public CutScene getOutroCutScene()
    {
        return new CutScene(null, "outro");
    }

    @Override
    protected void onStartPlaying()
    {
        Timer.schedule(new Timer.Task()
        {
            @Override
            public void run()
            {
                startDialog("bewegen");
            }
        }, 1.2f);
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        Trigger movementDialogTrigger = new Trigger("Bewegen Fertig");
        movementDialogTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                if (!movement)
                {
                    movement = true;
                    startDialog("bewegen_fertig");
                }
            }
        });

        Trigger interactionDialogTrigger = new Trigger("Interaktion");
        interactionDialogTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                if (!interaction)
                {
                    interaction = true;
                    startDialog("interagieren");
                }
            }
        });

        final StoneBarrier barrier1 = new StoneBarrier("InteraktionBarriere");
        final StoneBarrier barrier2 = new StoneBarrier("KampfBarriere");

        InteractionZone mushroom = new InteractionZone("Pilz");
        mushroom.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("pilz", new ActionCallback()
                {
                    @Override
                    public void run()
                    {
                        if (!interaction2)
                        {
                            interaction2 = true;
                            barrier1.destroy();
                            startDialog("interagieren_fertig");
                        }
                    }
                });
            }
        });

        Trigger battleInfo = new Trigger("Kampf");
        battleInfo.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                if (!battle)
                {
                    battle = true;
                    startDialog("kampf");
                }
            }
        });

        ActionCallback dummyCallback = new ActionCallback()
        {
            @Override
            public void run()
            {
                chickenCount--;
                if (chickenCount <= 0)
                    barrier2.destroy();
            }
        };

        TutorialDummy dummy1 = new TutorialDummy("Kampf Dummy 1");
        dummy1.setDeathCallback(dummyCallback);

        TutorialDummy dummy2 = new TutorialDummy("Kampf Dummy 2");
        dummy2.setDeathCallback(dummyCallback);

        TutorialDummy dummy3 = new TutorialDummy("Kampf Dummy 3");
        dummy3.setDeathCallback(dummyCallback);

        TutorialDummy dummy4 = new TutorialDummy("Kampf Dummy 4");
        dummy4.setDeathCallback(dummyCallback);

        Trigger battleTrigger = new Trigger("Kampf Fertig");
        battleTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                if (!battle2)
                {
                    battle2 = true;
                    startDialog("kampf_fertig");
                }
            }
        });

        Trigger exitTrigger = new Trigger("Ausgang");
        exitTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                changeLevel("introduction");
            }
        });

        worldConfig.registerObject(movementDialogTrigger);
        worldConfig.registerObject(interactionDialogTrigger);
        worldConfig.registerObject(barrier1);
        worldConfig.registerObject(barrier2);
        worldConfig.registerObject(mushroom);
        worldConfig.registerObject(battleInfo);
        worldConfig.registerObject(dummy1);
        worldConfig.registerObject(dummy2);
        worldConfig.registerObject(dummy3);
        worldConfig.registerObject(dummy4);
        worldConfig.registerObject(battleTrigger);
        worldConfig.registerObject(exitTrigger);
    }
}


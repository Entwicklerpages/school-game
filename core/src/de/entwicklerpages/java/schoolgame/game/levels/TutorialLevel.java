package de.entwicklerpages.java.schoolgame.game.levels;

import com.badlogic.gdx.utils.Timer;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.CutScene;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;

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
                            startDialog("interagieren_fertig");
                        }
                    }
                });
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
        worldConfig.registerObject(mushroom);
        worldConfig.registerObject(exitTrigger);
    }
}


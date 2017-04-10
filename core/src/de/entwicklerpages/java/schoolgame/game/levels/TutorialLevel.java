package de.entwicklerpages.java.schoolgame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.CutScene;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
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

    private boolean introDialog = false;

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
        }, 0.7f);
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        Trigger introDialogTrigger = new Trigger("Intro Dialog");
        introDialogTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                if (!introDialog)
                {
                    introDialog = true;
                    startDialog("bewegen_fertig");
                }
            }
        });


        Trigger chaosTrigger = new Trigger("Chaos Trigger");
        chaosTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                Gdx.app.log("DEBUG", "Trigger Chaos Entered");
            }
        });

        chaosTrigger.setTriggerLeaved(new ActionCallback()
        {
            @Override
            public void run()
            {
                Gdx.app.log("DEBUG", "Trigger Chaos Leaved");
                changeLevel("introduction");
            }
        });

        Trigger stormTrigger = new Trigger("Storm Trigger");
        stormTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                Gdx.app.log("DEBUG", "Trigger Storm Entered");
            }
        });

        stormTrigger.setTriggerLeaved(new ActionCallback()
        {
            @Override
            public void run()
            {
                Gdx.app.log("DEBUG", "Trigger Storm Leaved");
            }
        });

        worldConfig.registerObject(introDialogTrigger);
        worldConfig.registerObject(chaosTrigger);
        worldConfig.registerObject(stormTrigger);
    }
}


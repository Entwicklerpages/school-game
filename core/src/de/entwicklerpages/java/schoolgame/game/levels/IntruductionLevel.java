package de.entwicklerpages.java.schoolgame.game.levels;

import com.badlogic.gdx.utils.Timer;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.CutScene;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;

/**
 * Einstiegs Level
 *
 * LEVEL INFO
 * Erstes echte Level. Bringt dem Spieler die Story näher.
 *
 * @author nico
 */
@SuppressWarnings("unused") // Dise Klasse wird über Reflection geladen
public class IntruductionLevel extends Level {
    public static final String LEVEL_NAME = "introduction";

    public IntruductionLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Einführung";
    }

    @Override
    protected CutScene getIntroCutScene()
    {
        return new CutScene(null, "intro");
    }

    @Override
    protected void onStartPlaying()
    {
        Timer.schedule(new Timer.Task()
        {
            @Override
            public void run()
            {
                startDialog("intro2");
            }
        }, 1.2f);
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        InteractionZone bed = new InteractionZone("Bett");
        bed.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("bett");
            }
        });

        InteractionZone fortitudora = new InteractionZone("Heilerin");
        fortitudora.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("heilerin");
            }
        });

        InteractionZone bucket_empty = new InteractionZone("Eimer");
        bucket_empty.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("eimer");
            }
        });

        InteractionZone bucket_full = new InteractionZone("Wassereimer");
        bucket_full.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("wassereimer");
            }
        });

        InteractionZone letters = new InteractionZone("Briefe");
        letters.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("briefe");
            }
        });

        InteractionZone parchment = new InteractionZone("Pergamentrolle");
        parchment.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("pergamentrolle");
            }
        });

        InteractionZone kettle = new InteractionZone("Zauberkessel");
        kettle.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("zauberkessel");
            }
        });

        InteractionZone powder = new InteractionZone("Pulver");
        powder.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("pulver");
            }
        });

        InteractionZone cupboard1 = new InteractionZone("Trank_schrank1");
        cupboard1.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("trank_schrank1");
            }
        });

        InteractionZone cupboard2 = new InteractionZone("Trank_schrank2");
        cupboard2.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("trank_schrank2");
            }
        });

        InteractionZone spellbook = new InteractionZone("Zauberbuch");
        spellbook.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("zauberbuch");
            }
        });

        Trigger exitTrigger = new Trigger("Ausgang");
        exitTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                changeLevel("introduction_village");
            }
        });

        worldConfig.registerObject(bed);
        worldConfig.registerObject(fortitudora);
        worldConfig.registerObject(bucket_empty);
        worldConfig.registerObject(bucket_full);
        worldConfig.registerObject(letters);
        worldConfig.registerObject(parchment);
        worldConfig.registerObject(kettle);
        worldConfig.registerObject(powder);
        worldConfig.registerObject(cupboard1);
        worldConfig.registerObject(cupboard2);
        worldConfig.registerObject(spellbook);
        worldConfig.registerObject(exitTrigger);
    }
}

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
 * Erstes mal im Dorf. Bringt dem Spieler die Story näher.
 *
 * @author nico
 */
@SuppressWarnings("unused") // Dise Klasse wird über Reflection geladen
public class IntroductionVillageLevel extends Level
{
    public static final String LEVEL_NAME = "introduction_village";

    private boolean exitActive = false;

    public IntroductionVillageLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Einführung: Dorf";
    }

    @Override
    protected String getMusicName()
    {
        return "celtic_impulse";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        InteractionZone witch = new InteractionZone("Schild_Hexe");
        witch.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schild_hexe");
            }
        });

        InteractionZone legend = new InteractionZone("Legende");
        legend.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("legendenstein");
            }
        });

        InteractionZone warrior = new InteractionZone("Schild_Krieger");
        warrior.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schild_krieger");
            }
        });

        InteractionZone couple = new InteractionZone("Schild_Paerchen");
        couple.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schild_paerchen");
            }
        });

        InteractionZone smith = new InteractionZone("Schild_Schmied");
        smith.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schild_schmied");
            }
        });

        InteractionZone well = new InteractionZone("Brunnen");
        well.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("brunnen");
            }
        });

        InteractionZone signMayor = new InteractionZone("Schild_Buergermeister");
        signMayor.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schild_buergermeister");
            }
        });

        InteractionZone mayor = new InteractionZone("Buergermeister");
        mayor.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                if (!exitActive)
                {
                    startDialog("buergermeister");
                    exitActive = true;
                } else {
                    startDialog("buergermeister_fertig");
                }
            }
        });

        InteractionZone guard1 = new InteractionZone("Schild_Stultus");
        guard1.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schild_stultus");
            }
        });

        InteractionZone guard2 = new InteractionZone("Schild_Crasus");
        guard2.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schild_crasus");
            }
        });

        InteractionZone guard = new InteractionZone("Wache");
        guard.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("wache");
            }
        });

        final Trigger exitTrigger = new Trigger("Ausgang");
        exitTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                if (exitActive)
                    changeLevel("introduction_crossing");
            }
        });

        worldConfig.registerObject(witch);
        worldConfig.registerObject(legend);
        worldConfig.registerObject(warrior);
        worldConfig.registerObject(couple);
        worldConfig.registerObject(smith);
        worldConfig.registerObject(well);
        worldConfig.registerObject(signMayor);
        worldConfig.registerObject(mayor);
        worldConfig.registerObject(guard1);
        worldConfig.registerObject(guard2);
        worldConfig.registerObject(guard);
        worldConfig.registerObject(exitTrigger);
    }
}

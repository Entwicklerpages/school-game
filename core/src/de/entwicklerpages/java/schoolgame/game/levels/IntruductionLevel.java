package de.entwicklerpages.java.schoolgame.game.levels;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;

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
        super("introduction");
    }

    @Override
    public String getTitle() {
        return "Einführung";
    }

    @Override
    protected void onPrepare(WorldObjectManager.WorldObjectConfig worldConfig)
    {
        InteractionZone signInteraction = new InteractionZone("Schild");

        signInteraction.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schild");
            }
        });

        InteractionZone scullInteraction = new InteractionZone("Schaedel");

        scullInteraction.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("bar1");
            }
        });

        worldConfig.registerObject(signInteraction);
        worldConfig.registerObject(scullInteraction);
    }
}

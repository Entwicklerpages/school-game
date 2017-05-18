package de.entwicklerpages.java.schoolgame.game.levels;

import com.badlogic.gdx.Gdx;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Level;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;
import de.entwicklerpages.java.schoolgame.game.objects.InteractionZone;
import de.entwicklerpages.java.schoolgame.game.objects.Trigger;

/**
 * Nach der Höhle im Dorf
 *
 * LEVEL INFO
 * Ergebnisse des Kampfes
 *
 * @author nico
 */
public class CaveVillageLevel extends Level
{
    public static final String LEVEL_NAME = "cave_village";

    public CaveVillageLevel() {
        super(LEVEL_NAME);
    }

    @Override
    public String getTitle() {
        return "Höhle: Dorf";
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
            {startDialog("schild_buergermeister");
            }
        });

        InteractionZone mayor = new InteractionZone("Buergermeister");
        mayor.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("buergermeister");
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


        InteractionZone personSmith = new InteractionZone("Schmied");
        personSmith.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schmied");
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

        InteractionZone personKids = new InteractionZone("Kinder");
        personKids.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("kinder");
            }
        });

        InteractionZone personCouple = new InteractionZone("Paerchen");
        personCouple.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("paerchen");
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


        InteractionZone bed1 = new InteractionZone("Bett");
        bed1.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("bett");
            }
        });

        InteractionZone bed2 = new InteractionZone("Bett2");
        bed2.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("bett2");
            }
        });

        InteractionZone bed3 = new InteractionZone("Bett3");
        bed3.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("bett2");
            }
        });

        InteractionZone bed4 = new InteractionZone("Bett4");
        bed4.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("bett2");
            }
        });


        InteractionZone desktop1 = new InteractionZone("Schreibtisch1");
        desktop1.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schreibtisch");
            }
        });

        InteractionZone desktop2 = new InteractionZone("Schreibtisch2");
        desktop2.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schreibtisch2");
            }
        });

        InteractionZone desktop3 = new InteractionZone("Schreibtisch3");
        desktop3.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schreibtisch2");
            }
        });

        InteractionZone book = new InteractionZone("Buch");
        book.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("buch");
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

        InteractionZone cupboard3 = new InteractionZone("Schraenke1");
        cupboard3.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schraenke");
            }
        });

        InteractionZone cupboard4 = new InteractionZone("Schraenke2");
        cupboard4.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schraenke");
            }
        });

        InteractionZone cupboard5 = new InteractionZone("Schraenke3");
        cupboard5.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("schraenke");
            }
        });

        InteractionZone wardrobe1 = new InteractionZone("Kleiderschrank1");
        wardrobe1.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("kleiderschrank");
            }
        });

        InteractionZone wardrobe2 = new InteractionZone("Kleiderschrank2");
        wardrobe2.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("kleiderschrank");
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

        InteractionZone spellbook = new InteractionZone("Zauberbuch");
        spellbook.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("zauberbuch");
            }
        });

        InteractionZone broom1 = new InteractionZone("Besen");
        broom1.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("besen");
            }
        });

        InteractionZone broom2 = new InteractionZone("Besen2");
        broom2.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("besen2");
            }
        });


        InteractionZone letter1 = new InteractionZone("Brief1");
        letter1.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("brief1");
            }
        });

        InteractionZone letter2 = new InteractionZone("Brief2");
        letter2.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("brief2");
            }
        });

        InteractionZone letter3 = new InteractionZone("Brief3");
        letter3.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("brief3");
            }
        });

        InteractionZone letter4 = new InteractionZone("Brief4");
        letter4.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("brief4");
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

        InteractionZone letter_sina = new InteractionZone("Brief_Sina");
        letter_sina.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("brief_sina");
            }
        });

        InteractionZone letter_fina = new InteractionZone("Brief_Fina");
        letter_fina.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("brief_fina");
            }
        });

        InteractionZone bucket1 = new InteractionZone("Eimer1");
        bucket1.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("eimer");
            }
        });

        InteractionZone bucket2 = new InteractionZone("Eimer2");
        bucket2.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("eimer");
            }
        });

        InteractionZone bucket3 = new InteractionZone("Eimer3");
        bucket3.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("eimer");
            }
        });

        InteractionZone bucket4 = new InteractionZone("Eimer4");
        bucket4.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("eimer");
            }
        });

        InteractionZone bucket5 = new InteractionZone("Eimer5");
        bucket5.setActionCallback(new ActionCallback()
        {
            @Override
            public void run()
            {
                startDialog("eimer");
            }
        });

        Trigger house_witch = new Trigger("Haus_Hexe");
        house_witch.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                Gdx.app.postRunnable(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        player.setPosition(236, 135);
                    }
                });
            }
        });

        Trigger house_witch_exit = new Trigger("Ausgang_Hexe");
        house_witch_exit.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                Gdx.app.postRunnable(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        player.setPosition(580, 4230);
                    }
                });
            }
        });

        Trigger house_mayor = new Trigger("Haus_Buergermeister");
        house_mayor.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                Gdx.app.postRunnable(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        player.setPosition(1860, 135);
                    }
                });
            }
        });

        Trigger house_mayor_exit = new Trigger("Ausgang_Buergermeister");
        house_mayor_exit.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                Gdx.app.postRunnable(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        player.setPosition(1880, 5750);
                    }
                });
            }
        });

        final Trigger exitTrigger = new Trigger("Ausgang");
        exitTrigger.setTriggerEntered(new ActionCallback()
        {
            @Override
            public void run()
            {
                changeLevel("forest_crossing");
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

        worldConfig.registerObject(personSmith);
        worldConfig.registerObject(personWarrior);
        worldConfig.registerObject(personKids);
        worldConfig.registerObject(personCouple);
        worldConfig.registerObject(personWitch);

        worldConfig.registerObject(bed1);
        worldConfig.registerObject(bed2);
        worldConfig.registerObject(bed3);
        worldConfig.registerObject(bed4);

        worldConfig.registerObject(desktop1);
        worldConfig.registerObject(desktop2);
        worldConfig.registerObject(desktop3);
        worldConfig.registerObject(book);

        worldConfig.registerObject(cupboard1);
        worldConfig.registerObject(cupboard2);
        worldConfig.registerObject(cupboard3);
        worldConfig.registerObject(cupboard4);
        worldConfig.registerObject(cupboard5);
        worldConfig.registerObject(wardrobe1);
        worldConfig.registerObject(wardrobe2);

        worldConfig.registerObject(bucket_full);
        worldConfig.registerObject(powder);
        worldConfig.registerObject(parchment);
        worldConfig.registerObject(kettle);
        worldConfig.registerObject(spellbook);
        worldConfig.registerObject(broom1);
        worldConfig.registerObject(broom2);

        worldConfig.registerObject(letter1);
        worldConfig.registerObject(letter2);
        worldConfig.registerObject(letter3);
        worldConfig.registerObject(letter4);
        worldConfig.registerObject(letters);
        worldConfig.registerObject(letter_sina);
        worldConfig.registerObject(letter_fina);

        worldConfig.registerObject(bucket1);
        worldConfig.registerObject(bucket2);
        worldConfig.registerObject(bucket3);
        worldConfig.registerObject(bucket4);
        worldConfig.registerObject(bucket5);

        worldConfig.registerObject(house_witch);
        worldConfig.registerObject(house_witch_exit);
        worldConfig.registerObject(house_mayor);
        worldConfig.registerObject(house_mayor_exit);

        worldConfig.registerObject(exitTrigger);
    }
}

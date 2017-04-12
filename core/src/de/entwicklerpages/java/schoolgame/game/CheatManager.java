package de.entwicklerpages.java.schoolgame.game;

/**
 * Speichert aktivierte Cheats.
 * Ist ein Singleton, damit von überall darauf zugegriffen werden kann.
 * Wird vom normalen Spieler *eigentlich* nicht benutzt.
 *
 * @author nico
 */
public final class CheatManager {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////// SINGLETON /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gehört zum Singleton. Speichert die globale Instanz.
     */
    private static final CheatManager ourInstance = new CheatManager();

    /**
     * Gehört zum Singleton. Gibt die globale Instanz zurück.
     *
     * @return die globale Instanz
     */
    public static CheatManager getInstance() {
        return ourInstance;
    }

    /**
     * Privater Konstruktor.
     *
     * Verhindert, dass von außen eine Instanz erstellt werden kann.
     */
    private CheatManager() {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Aktiviert die Unsterblichkeit.
     * Wenn diese Eigenschaft true ist, bekommt der Spieler keinen schaden.
     */
    private boolean immortality = false;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////// GETTER & SETTER ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Ruft die Unsterblichkeit des Spielers ab.
     *
     * @return normal immer false, wenn unsterblich, dann true
     */
    public boolean isImmortal() {
        return immortality;
    }

    /**
     * Legt die Unsterblichkeit des Spielers fest.
     *
     * @param immortal neue Unsterblichkeit
     */
    public void setImmortality(boolean immortal) {
        this.immortality = immortal;
    }
}

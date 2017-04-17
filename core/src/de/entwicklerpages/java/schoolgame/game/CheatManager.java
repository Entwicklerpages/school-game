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

    /**
     * Macht den Spieler schneller.
     * Wenn diese Eigenschaft true ist, läuft der Spieler viel schneller.
     */
    private boolean superFast = false;

    /**
     * Erlaubt eine Veränderung der Gesundheit durch die Tasten J und K.
     */
    private boolean healthControl = false;

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

        setHealthControl(this.healthControl);
    }

    /**
     * Ruft ab, ob der Spieler super schnell ist.
     *
     * @return normal immer false, wenn extra schnell, dann true
     */
    public boolean isSuperFast()
    {
        return superFast;
    }

    /**
     * Legt fest, ob der Spieler super schnell ist.
     *
     * @param superFast true für extra schnell, false für normal
     */
    public void setSuperFast(boolean superFast)
    {
        this.superFast = superFast;
    }

    /**
     * Ruft ab, ob die Gesundheit des Spielers manuell verändert werden kann.
     * J veringert die Gesundheit,
     * K vergrößert die Gesundheit
     *
     * @return true für manuelle Kontrolle, false für normal
     */
    public boolean isHealthControlled()
    {
        return healthControl;
    }

    /**
     * Legt fest, ob die Gesundheit des Spielers manuell verändert werden kann.
     * J veringert die Gesundheit,
     * K vergrößert die Gesundheit
     *
     * Unsterblichkeit muss aus sein!
     *
     * @param healthControl true für manuelle Kontrolle, false für normal
     */
    public void setHealthControl(boolean healthControl)
    {
        this.healthControl = healthControl && !immortality;
    }
}

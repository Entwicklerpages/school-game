package de.entwicklerpages.java.schoolgame.game;

/**
 * Gibt die Ausrichtung des Spielers oder eines Entities an.
 *
 * @author nico
 * @see Player
 */
public enum EntityOrientation {

    /**
     * Nach Unten/Vorne.
     * Die Spielfigur schaut zum Display.
     *
     * @see EntityOrientation#LOOK_BACKWARD
     */
    LOOK_FORWARD,

    /**
     * Nach Links.
     * Die Spielfigur schaut auf dem Display nach Links.
     *
     * @see EntityOrientation#LOOK_RIGHT
     */
    LOOK_LEFT,

    /**
     * Nach Oben/Hinten.
     * Die Spielfigur schaut weg vom Display, man sieht nur den Rücken.
     *
     * @see EntityOrientation#LOOK_FORWARD
     */
    LOOK_BACKWARD,

    /**
     * Nach Rechts.
     * Die Spielfigur schaut auf dem Display nach Rechts.
     *
     * @see EntityOrientation#LOOK_LEFT
     */
    LOOK_RIGHT
}

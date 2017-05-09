package de.entwicklerpages.java.schoolgame.common;

/**
 * Allgemeines Interface für Callbacks
 *
 * Kann an jeder Stelle für ein Callback benutzt werden.
 *
 * @author nico
 *
 * @see de.entwicklerpages.java.schoolgame.menu.MenuState
 */
public interface ActionCallback {

    /**
     * Callback-Methode
     */
    void run();
}

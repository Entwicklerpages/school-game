package de.entwicklerpages.java.schoolgame.game.objects;

import de.entwicklerpages.java.schoolgame.game.Player;

/**
 * Interface für Klassen, dessen Objekte sich für Spielerinteraktionen registireren lassen können.
 *
 * @author nico
 */
public interface Interactable
{
    /**
     * Wird bei einer Spielerinteraktion ausgeführt.
     *
     * @param player der Spieler
     */
    void interaction(Player player);

    /**
     * Ruft die Priorität dieses Objektes ab.
     * Desso größer die Zahl, desso wichtiger ist das Objekt.
     *
     * @return die Priorität
     */
    int getInteractionPriority();
}

package de.entwicklerpages.java.schoolgame.game.objects;

import de.entwicklerpages.java.schoolgame.game.Player;

/**
 * Interface für Klassen, dessen Objekte sich für Spielerangriffe registrieren lassen können.
 *
 * @author nico
 */
public interface Attackable
{
    /**
     * Wird bei einer Spielerinteraktion ausgeführt.
     *
     * @param player der Spieler
     * @return true, wenn das Objekt auf den Angriff reagiert hat.
     */
    boolean onPlayerAttack(Player player, int damage);
}

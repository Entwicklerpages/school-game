package de.entwicklerpages.java.schoolgame.game.objects;

import de.entwicklerpages.java.schoolgame.game.Player;

/**
 * @author nico
 */
public interface Interactable
{
    void interaction(Player player);
    int getInteractionPriority();
}

package de.entwicklerpages.java.schoolgame.game;

public final class CheatManager {

    // SINGLETON

    private static CheatManager ourInstance = new CheatManager();

    public static CheatManager getInstance() {
        return ourInstance;
    }

    private CheatManager() {
    }

    // MEMBERS

    private boolean immortality = false;


    // SETTERS & GETTERS


    public boolean isImmortal() {
        return immortality;
    }

    public void setImmortality(boolean immortal) {
        this.immortality = immortal;
    }
}

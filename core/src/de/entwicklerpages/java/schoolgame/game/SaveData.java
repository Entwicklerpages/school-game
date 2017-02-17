package de.entwicklerpages.java.schoolgame.game;


import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.TimeUtils;

import java.sql.Time;
import java.util.ArrayList;

import de.entwicklerpages.java.schoolgame.SchoolGame;

public class SaveData {
    private static final String PLAYER_NAME = "_PLAYER_NAME";
    private static final String MALE = "_MALE";
    private static final String PLAY_TIME = "_PLAY_TIME";
    private static final String LEVEL_NAME = "_LEVEL_NAME";

    private Preferences prefs;
    private Slot slot;

    private String playerName = "";
    private boolean male = false;
    private long playTime = 0;
    private String levelName = "";

    private long startTime = 0;

    public SaveData(SchoolGame game, Slot slot)
    {
        this.prefs = game.getPreferences();
        this.slot = slot;

        this.load();
    }

    public void load()
    {
        startTime = TimeUtils.millis();

        if (this.slot == null) return;
        
        playerName = prefs.getString(slot.name() + PLAYER_NAME, "");
        male = prefs.getBoolean(slot.name() + MALE, false);
        playTime = prefs.getLong(slot.name() + PLAY_TIME, 0);
        levelName = prefs.getString(slot.name() + LEVEL_NAME, "");
    }

    public void save(Slot slot)
    {
        if (slot == null)
            slot = this.slot;
        else
            this.slot = slot;

        if (this.slot == null) return;

        playTime += TimeUtils.timeSinceMillis(startTime);

        prefs.putString(slot.name() + PLAYER_NAME, playerName);
        prefs.putBoolean(slot.name() + MALE, male);
        prefs.putLong(slot.name() + PLAY_TIME, playTime);
        prefs.putString(slot.name() + LEVEL_NAME, levelName);
        prefs.flush();

        startTime = TimeUtils.millis();

    }

    public void reset()
    {
        playerName = "";
        male = false;
        playTime = 0;

        prefs.remove(slot.name() + PLAYER_NAME);
        prefs.remove(slot.name() + MALE);
        prefs.remove(slot.name() + PLAY_TIME);
        prefs.remove(slot.name() + LEVEL_NAME);
        prefs.flush();
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Slot getSlot() {
        return slot;
    }

    public boolean isUsed() {
        return playTime != 0 && playerName.length() > 0;
    }

    public static void clearSlot(SchoolGame game, Slot slot)
    {
        SaveData saveData = new SaveData(game, slot);
        saveData.reset();
    }

    public static SaveData[] getAll(SchoolGame game)
    {
        ArrayList<SaveData>savedSlots = new ArrayList<SaveData>();

        for (Slot slot: Slot.values())
        {
            savedSlots.add(new SaveData(game, slot));
        }

        return savedSlots.toArray(new SaveData[savedSlots.size()]);
    }

    public enum Slot
    {
        SLOT_1,
        SLOT_2,
        SLOT_3,
        SLOT_4,
        SLOT_5
    }
}

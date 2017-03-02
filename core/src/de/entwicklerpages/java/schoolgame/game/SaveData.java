package de.entwicklerpages.java.schoolgame.game;


import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

import de.entwicklerpages.java.schoolgame.SchoolGame;

/**
 * Gibt Zugriff auf die Informationene eines Spielstandes.
 * Diese Klasse ist auch für das Laden und Speichern der Spielzustände verantwortlich.
 *
 * @author nico
 */
public class SaveData {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////// STATISCHE EIGENSCHAFTEN ////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static final String PLAYER_NAME = "_PLAYER_NAME";
    private static final String MALE = "_MALE";
    private static final String PLAY_TIME = "_PLAY_TIME";
    private static final String LEVEL_NAME = "_LEVEL_NAME";
    private static final String LEVEL_ID = "_LEVEL_ID";

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Zugriff auf die Spieleigenschaften.
     *
     * @see SchoolGame#getPreferences()
     */
    private Preferences prefs;

    /**
     * Speichert, in welchem Slot die Daten gesichert werden.
     */
    private Slot slot;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ABRUFBARE EIGENSCHAFTEN

    /**
     * Der Name des Spielers/des Slots.
     */
    private String playerName = "";

    /**
     * Das Geschlecht der Spielfigur.
     */
    private boolean male = false;

    /**
     * Die gesammte Spielzeit innerhalb dieses Speicherstandes.
     */
    private long playTime = 0;

    /**
     * Der Name des aktuellen Levels.
     * Wird nur für die Anzeige benutzt.
     */
    private String levelName = "";

    /**
     * Die ID des aktuellen Levels.
     * Wird zum Laden benötigt.
     */
    private String levelId = "";

    /**
     * Speichert die Systemzeit, als zuletzt geladen oder gespeichert wurde.
     * Kann nicht direkt abgerufen werden, dient aber zur Berechnung der gesamten
     * Spielzeit.
     */
    private long startTime = 0;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////// METHODEN //////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Standardkonstruktor.
     *
     * @param game Zugriff auf die Spielinstanz
     * @param slot der Slot, der benutzt werden soll
     */
    public SaveData(SchoolGame game, Slot slot)
    {
        this.prefs = game.getPreferences();
        this.slot = slot;

        this.load();
    }

    /**
     * Lädt die Daten aus der Speicherdatei.
     *
     * @see SaveData#save(Slot)
     */
    public void load()
    {
        startTime = TimeUtils.millis();

        if (this.slot == null) return;
        
        playerName = prefs.getString(slot.name() + PLAYER_NAME, "");
        male = prefs.getBoolean(slot.name() + MALE, false);
        playTime = prefs.getLong(slot.name() + PLAY_TIME, 0);
        levelName = prefs.getString(slot.name() + LEVEL_NAME, "");
        levelId = prefs.getString(slot.name() + LEVEL_ID, "");
    }

    /**
     * Speichert die Daten in die Spieldatei.
     *
     * @param slot der Slot in den gespeichert werden soll. Wenn null, wird der letzte Slot verwendet
     *
     * @see SaveData#load()
     */
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
        prefs.putString(slot.name() + LEVEL_ID, levelId);
        prefs.flush();

        startTime = TimeUtils.millis();

    }

    /**
     * Setzt diesen Slot zurück.
     */
    public void reset()
    {
        playerName = "";
        male = false;
        playTime = 0;
        levelName = "";
        levelId = "";

        prefs.remove(slot.name() + PLAYER_NAME);
        prefs.remove(slot.name() + MALE);
        prefs.remove(slot.name() + PLAY_TIME);
        prefs.remove(slot.name() + LEVEL_NAME);
        prefs.remove(slot.name() + LEVEL_ID);
        prefs.flush();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////// GETTER & SETTER ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Ruft den Spielernamen ab.
     *
     * @return der Spielername
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Legt einen neuen Spielernamen fest.
     *
     * @param playerName der neue Name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Ruft das Geschlecht der Spielfigur ab.
     *
     * @return true, wenn die Figur männlich ist, false wenn sie weiblich ist.
     */
    public boolean isMale() {
        return male;
    }

    /**
     * Legt das Geschlecht der Spielfigur fest.
     *
     * @param male das neue Geschlecht, true für männlich, false für weiblich
     */
    public void setMale(boolean male) {
        this.male = male;
    }

    /**
     * Ruft die gesamte Spielzeit ab.
     *
     * @return die Spielzeit in Millisekunden
     */
    public long getPlayTime() {
        return playTime;
    }

    /**
     * Legt eine neue, gesamte Spielzeit fest.
     * Diese Methode sollte nicht benutzt werden, die Spielzeit wird bereits
     * innerhalb dieser Klasse erfasst.
     *
     * @param playTime die neue Spielzeit
     * @deprecated SaveData erfässt die Spielzeit selbst
     */
    @Deprecated
    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    /**
     * Gibt den Namen des aktuellen Levels zurück.
     *
     * @return der Name des aktuellen Levels
     */
    public String getLevelName() {
        return levelName;
    }

    /**
     * Legt den Namen des aktuellen Levels fest.
     *
     * @param levelName der Name des aktuellen Levels
     */
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    /**
     * Ruft die ID des aktuellen Levels ab.
     *
     * @return die LevelID
     */
    public String getLevelId() {
        return levelId;
    }

    /**
     * Ändert das aktuelle Level indem eine neue ID zugeordnet wird.
     *
     * @param levelId die neue LevelID
     */
    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    /**
     * Gibt den Slot zurück, der derzeit zum Laden und Speichern verwendet wird.
     *
     * @return der Slot
     */
    public Slot getSlot() {
        return slot;
    }

    /**
     * Prüft, ob dieser Speicherstand in benutzung ist.
     *
     * @return true, wenn er verwendet wird, false wenn nicht
     */
    public boolean isUsed() {
        return playTime != 0 && playerName.length() > 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////// STATISCHE METHODEN /////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Löscht den angegebenen Slot.
     *
     * @param game Zugriff auf die Spielinstanz
     * @param slot der Slot, der gelöscht werden soll
     */
    public static void clearSlot(SchoolGame game, Slot slot)
    {
        SaveData saveData = new SaveData(game, slot);
        saveData.reset();
    }

    /**
     * Gibt eine Liste mit allen geladenen Slots zurück.
     *
     * @param game Zugriff auf die Spielinstanz
     * @return Ein Array mit allen Slots als Objekte.
     */
    public static SaveData[] getAll(SchoolGame game)
    {
        ArrayList<SaveData>savedSlots = new ArrayList<SaveData>();

        for (Slot slot: Slot.values())
        {
            savedSlots.add(new SaveData(game, slot));
        }

        return savedSlots.toArray(new SaveData[savedSlots.size()]);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ENDE

    /**
     * Alle Slots werden hier aufgeführt.
     *
     * @author nico
     */
    public enum Slot
    {
        SLOT_1,
        SLOT_2,
        SLOT_3,
        SLOT_4,
        SLOT_5
    }
}

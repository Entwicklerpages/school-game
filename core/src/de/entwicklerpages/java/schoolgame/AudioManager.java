package de.entwicklerpages.java.schoolgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;

/**
 * AudioManager
 * Die Spielmusik wird komplett über den AudioManager gesteuert.
 *
 * Anmerkung: Sounds werden eventuell in Zukunft auch über den AudioManager geregelt.
 *
 * @author nico
 */
public class AudioManager implements Disposable {

    /**
     * Zugriff auf die Spielinstanz
     *
     * @see SchoolGame
     */
    private SchoolGame game;

    /**
     * Ist die Musik stumm (gestoppt)?
     *
     * @see AudioManager#isMusicMuted()
     * @see AudioManager#setMuteMusic(boolean)
     */
    private boolean muteMusic;

    /**
     * Instanz des aktuell laufenden Stücks.
     *
     * @see AudioManager#selectMusic(String, float)
     * @see AudioManager#selectMusic(String, float, float)
     */
    private Music music = null;

    /**
     * Dateiname des aktuellen Stücks.
     */
    private String musicName = null;

    /**
     * Relative Lautstärke des aktuellen Stücks.
     */
    private float relativeMusicVolume = 1.0f;

    /**
     * Speichert den Namen des nächsten Liedes bei einem Fade.
     */
    private String newMusic = null;

    /**
     * Speichert den aktuellen Zustand des Fades.
     */
    private float fade = 0f;

    /**
     * Speichert die gesammte Fade-Zeit.
     */
    private float fadeTime = 0;

    /**
     * Standardkonstruktor
     *
     * @param game Spielinstanz
     */
    public AudioManager(SchoolGame game)
    {
        this.game = game;

        muteMusic = game.getPreferences().getBoolean("mute_music", false);
    }

    /**
     * Wählt ein neues Lied aus oder stoppt die Musik.
     *
     * @param name Der Dateiname, null zum stoppen
     *
     * @see AudioManager#selectMusic(String, float)
     * @see AudioManager#selectMusic(String, float, float)
     */
    public void selectMusic(String name)
    {
        this.selectMusic(name, 0f, 1.0f);
    }

    /**
     * Wählt ein neues Lied aus und startet einen Fade wenn die Fadezeit größer als 0.05f ist.
     *
     * @param name Der Dateiname, null zum stoppen
     * @param fadeTime Die Zeit bis das alte Lied ausgefadet ist
     *
     * @see AudioManager#selectMusic(String)
     * @see AudioManager#selectMusic(String, float, float)
     */
    public void selectMusic(String name, float fadeTime)
    {
        this.selectMusic(name, fadeTime, 1.0f);
    }

    /**
     * Wählt ein neues Lied aus und startet einen Fade wenn die Fadezeit größer als 0.05f ist.
     * Außerdem kann die Lautstärke des Liedes verändert werden.
     *
     * @param name Der Dateiname, null zum stoppen
     * @param fadeTime Die Zeit bis das alte Lied ausgefadet ist
     * @param relativeMusicVolume Die relative Lautstärke zwischen 0.0f und 1.0f
     *
     * @see AudioManager#selectMusic(String)
     * @see AudioManager#selectMusic(String, float)
     */
    public void selectMusic(String name, float fadeTime, float relativeMusicVolume)
    {
        this.relativeMusicVolume = relativeMusicVolume;

        if (name != null && music != null && name.equals(musicName)){
            music.setVolume(relativeMusicVolume);
            return;
        }

        resetFade();

        if (music == null)
        {
            loadMusic(name);
        } else if (fadeTime < 0.05f) {
            loadMusic(name);
        } else {
            this.fadeTime = fadeTime;
            newMusic = name;
            fade = fadeTime;
        }
    }

    /**
     * Lädt die Musik und startet sie.
     *
     * @param name Der Dateiname
     */
    private void loadMusic(String name)
    {
        stopMusic();

        if (name == null) {
            return;
        }

        musicName = name;
        if (muteMusic) return;

        music = Gdx.audio.newMusic(Gdx.files.internal("music/" + name + ".ogg"));
        music.setLooping(true);
        music.setVolume(relativeMusicVolume);
        music.play();
    }

    /**
     * Hilfsfunktion zum entfernen eines Faders.
     */
    private void resetFade()
    {
        this.fadeTime = 0f;
        this.fade = 0f;
        this.newMusic = null;
    }

    /**
     * Hilfsfunktion zum stoppen der Musik.
     *
     * Dabei wird auch gleich die Dispose Methode aufgerufen und die Referenz auf null gesetzt.
     *
     * @see AudioManager#music
     */
    private void stopMusic()
    {
        if (music != null)
        {
            music.stop();
            music.dispose();
            music = null;
        }
    }

    /**
     * Wird bei jedem Frame aufgerufen.
     *
     * Sorgt dafür, dass sich die Lautstärke bei einem Fade verändert.
     *
     * @param deltaTime Zeit, die seit dem letztem Frame vergangen ist.
     */
    public void update(float deltaTime)
    {
        if (music != null && fadeTime > 0.05f)
        {
            fade -= deltaTime;

            music.setVolume(relativeMusicVolume * (Math.max(fade, 0.0f)/fadeTime)); // Math.max verhindert, dass die Lautstärke negativ wird.

            if (fade <= 0.05f)
            {
                loadMusic(newMusic);

                resetFade();
            }
        }
    }

    /**
     * Wird aufgerufen, wenn der AudioManager nicht mehr benötigt wird.
     * Sollte noch Musik geladen sein, wird sie gestoppt und entladen.
     */
    public void dispose()
    {
        stopMusic();
    }

    /**
     * Stoppt oder startet die Musik und speichert die Einstellung.
     *
     * @param muteMusic true zum stoppen, false zum starten
     *
     * @see AudioManager#isMusicMuted()
     */
    public void setMuteMusic(boolean muteMusic)
    {
        if (this.muteMusic == muteMusic) return;

        this.muteMusic = muteMusic;

        if (muteMusic)
        {
            stopMusic();
        } else {
            loadMusic(musicName);
        }

        game.getPreferences().putBoolean("mute_music", muteMusic);
        game.getPreferences().flush();
    }

    /**
     * Ist die Musik gestoppt?
     *
     * @return aktueller Zustand
     *
     * @see AudioManager#setMuteMusic(boolean)
     */
    public boolean isMusicMuted() {
        return muteMusic;
    }
}

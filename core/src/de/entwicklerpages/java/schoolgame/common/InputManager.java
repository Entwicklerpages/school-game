package de.entwicklerpages.java.schoolgame.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import java.util.Map;

import de.entwicklerpages.java.schoolgame.SchoolGame;

/**
 * Hilft bei der Abfrage von Eingaben
 *
 * @author nico
 */
public final class InputManager implements ControllerListener
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////// SINGLETON /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gehört zum Singleton. Speichert die globale Instanz.
     */
    private static final InputManager ourInstance = new InputManager();

    /**
     * Gehört zum Singleton. Gibt die globale Instanz zurück.
     *
     * @return die globale Instanz
     */
    public static InputManager getInstance() {
        return ourInstance;
    }

    /**
     * Privater Konstruktor.
     *
     * Verhindert, dass von außen eine Instanz erstellt werden kann.
     */
    private InputManager() {
        Controllers.addListener(this);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Zugriff auf die Spielinstanz
     *
     * @see SchoolGame
     */
    private SchoolGame game;

    private InputProcessor feedForwardProcessor = null;

    private boolean gameMode = false;

    private Map<Integer, Action>controllerGameKeyBinding;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////// GETTER & SETTER ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void setGame(SchoolGame game)
    {
        this.game = game;
    }

    public void setFeedForwardProcessor(InputProcessor feedForwardProcessor)
    {
        this.feedForwardProcessor = feedForwardProcessor;
        gameMode = false;
    }

    public void requestGameMode()
    {
        gameMode = true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Prüft ob EINER der übergebenen Key Codes dem gedrücktem entspricht.
     *
     * Logisches ODER.
     *
     * @deprecated Der InputManager prüft die mehrere Tasten gleichzeitig.
     *
     * @param keycode der Keycode der gedrückten Tase
     * @param keys die Tasten die abgefragt werden sollen
     * @return true wenn eine der Tasten gedrückt wurde, sonst false
     */
    @Deprecated
    public static boolean checkKeys(int keycode, int... keys)
    {
        for (int key : keys)
        {
            if (key == keycode) return true;
        }

        return false;
    }

    public static Action checkMenuAction(int keycode)
    {
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W)
        {
            return Action.MOVE_UP;
        }

        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
        {
            return Action.MOVE_DOWN;
        }

        if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE)
        {
            return Action.INTERACTION;
        }

        return Action.UNKNOWN;
    }

    public static Action checkGameAction(int keycode)
    {
        if (keycode == Input.Keys.SPACE)
        {
            return Action.ATTACK;
        }

        if (keycode == Input.Keys.ENTER || keycode == Input.Keys.E)
        {
            return Action.INTERACTION;
        }

        if (keycode == Input.Keys.ESCAPE)
        {
            return Action.MENU;
        }

        if (keycode == Input.Keys.J)
        {
            return Action.CHEAT_DAMAGE;
        }

        if (keycode == Input.Keys.K)
        {
            return Action.CHEAT_HEAL;
        }

        return Action.UNKNOWN;
    }

    public static boolean checkActionActive(Action action)
    {
        switch (action)
        {
            case MOVE_UP:
                return Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);

            case MOVE_DOWN:
                return Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);

            case MOVE_LEFT:
                return Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);

            case MOVE_RIGHT:
                return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);

            case RUN:
                return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

            default:
                break;
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////// CONTROLLER ////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void connected(Controller controller)
    {

    }

    @Override
    public void disconnected(Controller controller)
    {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        if (feedForwardProcessor == null) return false;

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    {
        if (feedForwardProcessor == null) return false;

        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value)
    {
        if (feedForwardProcessor == null) return false;

        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value)
    {
        if (feedForwardProcessor == null) return false;

        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
    {
        return false;
    }

    /**
     * Listet mögliche Eingaben auf.
     *
     * @author nico
     */
    public enum Action {
        UNKNOWN,
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
        INTERACTION,
        RUN,
        ATTACK,
        MENU,
        CHEAT_DAMAGE,
        CHEAT_HEAL
    }
}

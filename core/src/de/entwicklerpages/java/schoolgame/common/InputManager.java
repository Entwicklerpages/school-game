package de.entwicklerpages.java.schoolgame.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
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

        controllerGameButtonBinding = new HashMap<ButtonKey, Action>();
        controllerGameAxisBinding = new HashMap<AxisKey, Action>();
        controllerGameAxisState = new HashMap<AxisKey, Boolean>();
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

    private Map<ButtonKey, Action>controllerGameButtonBinding;
    private Map<AxisKey, Action>controllerGameAxisBinding;
    private Map<AxisKey, Boolean>controllerGameAxisState;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////// GETTER & SETTER ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void setGame(SchoolGame game)
    {
        this.game = game;

        if (Controllers.getControllers().size > 0)
        {
            String name = Controllers.getControllers().first().getName();

            if (name.contains("Sony Computer Entertainment"))
            {
                controllerGameButtonBinding.put(new ButtonKey(1, name), Action.ATTACK);
                controllerGameButtonBinding.put(new ButtonKey(2, name), Action.INTERACTION);
                controllerGameButtonBinding.put(new ButtonKey(9, name), Action.INGAME_MENU);
                controllerGameButtonBinding.put(new ButtonKey(13, name), Action.INGAME_MENU);

                controllerGameAxisBinding.put(new AxisKey(1, true, name), Action.MOVE_DOWN);
                controllerGameAxisBinding.put(new AxisKey(1, false, name), Action.MOVE_UP);
            }
        }
    }

    public void setFeedForwardProcessor(InputProcessor feedForwardProcessor)
    {
        this.feedForwardProcessor = feedForwardProcessor;
    }

    public void requestGameMode()
    {
        gameMode = true;
    }

    public void requestMenuMode()
    {
        gameMode = false;
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
            return Action.MENU_SELECT;
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
            return Action.INGAME_MENU;
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
                return Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) || checkController(action);

            case MOVE_DOWN:
                return Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) || checkController(action);

            case MOVE_LEFT:
                return Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) || checkController(action);

            case MOVE_RIGHT:
                return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) || checkController(action);

            case RUN:
                return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT) || checkController(action);

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

        ButtonKey button = new ButtonKey(buttonCode, controller.getName());

        if (!controllerGameButtonBinding.containsKey(button)) return false;

        Action action = controllerGameButtonBinding.get(button);

        return controllerDown(action);
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    {
        if (feedForwardProcessor == null) return false;

        ButtonKey button = new ButtonKey(buttonCode, controller.getName());

        if (!controllerGameButtonBinding.containsKey(button)) return false;

        Action action = controllerGameButtonBinding.get(button);

        return controllerUp(action);
    }

    private boolean axisReleased(Controller controller, int axisCode)
    {
        AxisKey lowAxis = new AxisKey(axisCode, false, controller.getName());


        if (controllerGameAxisState.containsKey(lowAxis) && controllerGameAxisState.get(lowAxis))
        {
            controllerGameAxisState.put(lowAxis, false);
            return controllerUp(controllerGameAxisBinding.get(lowAxis));
        }

        AxisKey highAxis = new AxisKey(axisCode, true, controller.getName());

        if (controllerGameAxisState.containsKey(highAxis) && controllerGameAxisState.get(highAxis))
        {
            controllerGameAxisState.put(highAxis, false);
            return controllerUp(controllerGameAxisBinding.get(highAxis));
        }

        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value)
    {
        if (feedForwardProcessor == null) return false;

        boolean high;

        if (value > 0.7f)
            high = true;
        else if (value < -0.7f)
            high = false;
        else
            return axisReleased(controller, axisCode);

        AxisKey axis = new AxisKey(axisCode, high, controller.getName());

        if (!controllerGameAxisBinding.containsKey(axis)) return false;

        if (!controllerGameAxisState.containsKey(axis))
        {
            controllerGameAxisState.put(axis, true);
        } else {
            if (controllerGameAxisState.get(axis))
                return false;
            else
                controllerGameAxisState.put(axis, true);
        }

        controllerDown(controllerGameAxisBinding.get(axis));

        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value)
    {
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

    private static boolean checkController(Action action)
    {
        return false;
    }

    private boolean controllerDown(Action action)
    {
        if (!gameMode)
        {
            switch (action)
            {
                case MOVE_UP:
                    feedForwardProcessor.keyDown(Input.Keys.UP);
                    return true;

                case MOVE_DOWN:
                    feedForwardProcessor.keyDown(Input.Keys.DOWN);
                    return true;

                case INTERACTION:
                    feedForwardProcessor.keyDown(Input.Keys.ENTER);
                    return true;

                case ATTACK:
                    feedForwardProcessor.keyDown(Input.Keys.ENTER);
                    return true;

                default:
                    break;
            }
        } else {
            switch (action)
            {
                case INTERACTION:
                    feedForwardProcessor.keyDown(Input.Keys.ENTER);
                    return true;

                case ATTACK:
                    feedForwardProcessor.keyDown(Input.Keys.SPACE);
                    return true;

                case INGAME_MENU:
                    feedForwardProcessor.keyDown(Input.Keys.ESCAPE);
                    return true;
            }
        }

        return false;
    }

    private boolean controllerUp(Action action)
    {
        if (!gameMode)
        {
            switch (action)
            {
                case MOVE_UP:
                    feedForwardProcessor.keyUp(Input.Keys.UP);
                    return true;

                case MOVE_DOWN:
                    feedForwardProcessor.keyUp(Input.Keys.DOWN);
                    return true;

                case INTERACTION:
                    feedForwardProcessor.keyUp(Input.Keys.ENTER);
                    return true;

                case ATTACK:
                    feedForwardProcessor.keyUp(Input.Keys.ENTER);
                    return true;

                default:
                    break;
            }
        } else {
            switch (action)
            {
                case INTERACTION:
                    feedForwardProcessor.keyUp(Input.Keys.ENTER);
                    return true;

                case ATTACK:
                    feedForwardProcessor.keyUp(Input.Keys.SPACE);
                    return true;

                case INGAME_MENU:
                    feedForwardProcessor.keyUp(Input.Keys.ESCAPE);
                    return true;
            }
        }

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
        INGAME_MENU,
        CHEAT_DAMAGE,
        CHEAT_HEAL,
        MENU_SELECT
    }

    private class ButtonKey {
        private int buttonCode;
        private String controllerName;

        public ButtonKey(int buttonCode, String controllerName)
        {
            this.buttonCode = buttonCode;
            this.controllerName = controllerName;
        }

        public int getButtonCode()
        {
            return buttonCode;
        }

        public void setButtonCode(int buttonCode)
        {
            this.buttonCode = buttonCode;
        }

        public String getControllerName()
        {
            return controllerName;
        }

        public void setControllerName(String controllerName)
        {
            this.controllerName = controllerName;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ButtonKey buttonKey = (ButtonKey) o;

            if (buttonCode != buttonKey.buttonCode) return false;
            return controllerName != null ? controllerName.equals(buttonKey.controllerName) : buttonKey.controllerName == null;

        }

        @Override
        public int hashCode()
        {
            int result = buttonCode;
            result = 31 * result + (controllerName != null ? controllerName.hashCode() : 0);
            return result;
        }
    }

    private class AxisKey {
        private int axisCode;
        private boolean high;
        private String controllerName;

        public AxisKey(int axisCode, boolean high, String controllerName)
        {
            this.axisCode = axisCode;
            this.high = high;
            this.controllerName = controllerName;
        }

        public int getAxisCode()
        {
            return axisCode;
        }

        public void setAxisCode(int axisCode)
        {
            this.axisCode = axisCode;
        }

        public boolean isHigh()
        {
            return high;
        }

        public void setHigh(boolean high)
        {
            this.high = high;
        }

        public String getControllerName()
        {
            return controllerName;
        }

        public void setControllerName(String controllerName)
        {
            this.controllerName = controllerName;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AxisKey axisKey = (AxisKey) o;

            if (axisCode != axisKey.axisCode) return false;
            if (high != axisKey.high) return false;
            return controllerName != null ? controllerName.equals(axisKey.controllerName) : axisKey.controllerName == null;

        }

        @Override
        public int hashCode()
        {
            int result = axisCode;
            result = 31 * result + (high ? 1 : 0);
            result = 31 * result + (controllerName != null ? controllerName.hashCode() : 0);
            return result;
        }
    }
}

package de.entwicklerpages.java.schoolgame.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.Arrays;

public class CheatDetector extends InputAdapter {

    private int[] keySequence;

    private int[] previousKeys;

    private ActionCallback callback;

    public CheatDetector()
    {
        this.keySequence = null;
    }

    public void setKeySequence(int[] keySequence) {
        this.keySequence = keySequence;
        this.previousKeys = new int[keySequence.length];
    }

    public void setCallback(ActionCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keySequence != null && keySequence.length > 0)
        {
            shiftKeys(keycode);
            if (Arrays.equals(keySequence, previousKeys))
            {
                callback.run();
                return true;
            }
        }
        return false;
    }

    private void shiftKeys(int keycode)
    {
        for (int i = 0; i < previousKeys.length - 1; i++)
        {
            previousKeys[i] = previousKeys[i + 1];
        }
        previousKeys[previousKeys.length - 1] = keycode;
    }

}

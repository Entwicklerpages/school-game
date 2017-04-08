package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

import de.entwicklerpages.java.schoolgame.common.InputHelper;

public class Player {

    private static final float SPEED = 60f;
    private static final float ACCELERATION = 0.3f;
    private static final float RUN_FACTOR = 3f;
    private static final long LONG_ATTACK_TIME = 1700L;

    private int health;
    private String name;
    private boolean male;

    private float posX;
    private float posY;

    private float lastDeltaX = 0;
    private float lastDeltaY = 0;

    private long attackStart = -1;

    private EntityOrientation orientation;

    private ShapeRenderer shapeRenderer;

    public Player(String name, boolean male) {
        this.health = 100;
        this.name = name;
        this.male = male;
        this.posX = 0;
        this.posY = 0;
        this.orientation = EntityOrientation.LOOK_FORWARD;

        shapeRenderer = new ShapeRenderer();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health > 100)
            health = 100;
        if (health < 0)
            health = 0;

        this.health = health;
    }

    public void applyDamage(int damage) {
        this.setHealth(health - damage);
    }

    public void heal(int healAmount) {
        this.setHealth(health + healAmount);
    }

    public String getName() {
        return name;
    }

    public boolean isMale() {
        return male;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public EntityOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(EntityOrientation orientation) {
        this.orientation = orientation;
    }

    public void setPosition(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void update(float deltaTime) {
        float deltaX = 0;
        float deltaY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) )
        {
            deltaY += SPEED;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) )
        {
            deltaX -= SPEED;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) )
        {
            deltaY -= SPEED;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) )
        {
            deltaX += SPEED;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT))
        {
            deltaX *= RUN_FACTOR;
            deltaY *= RUN_FACTOR;
        }

        if (!MathUtils.isZero(deltaX))
        {
            orientation = deltaX > 0 ? EntityOrientation.LOOK_RIGHT : EntityOrientation.LOOK_LEFT;
        }

        if (!MathUtils.isZero(deltaY))
        {
            orientation = deltaY > 0 ? EntityOrientation.LOOK_BACKWARD : EntityOrientation.LOOK_FORWARD;
        }

        lastDeltaX = MathUtils.lerp(lastDeltaX, deltaX, ACCELERATION);
        lastDeltaY = MathUtils.lerp(lastDeltaY, deltaY, ACCELERATION);

        this.posX += lastDeltaX * deltaTime;
        this.posY += lastDeltaY * deltaTime;
    }

    public void render(OrthographicCamera camera, float deltaTime)
    {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(posX, posY, 16);
        shapeRenderer.end();
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(posX, posY + 12, 8);
        shapeRenderer.end();
    }

    public boolean keyDown(int keycode)
    {
        if (keycode == Input.Keys.SPACE)
        {
            // Attack
            Gdx.app.log("INFO", "Start Attack");
            attackStart = TimeUtils.millis();
            return true;
        }
        else if (InputHelper.checkKeys(keycode, Input.Keys.E, Input.Keys.ENTER))
        {
            // Interaction
            Gdx.app.log("INFO", "Interaction");
            return true;
        }

        return false;
    }

    public boolean keyUp(int keycode)
    {
        if (keycode == Input.Keys.SPACE && attackStart > 0)
        {
            // Attack

            if (TimeUtils.timeSinceMillis(attackStart) >= LONG_ATTACK_TIME)
            {
                Gdx.app.log("INFO", "Stop Long Attack");
            } else {
                Gdx.app.log("INFO", "Stop Short Attack");
            }

            attackStart = -1;
            return true;
        }
        return false;
    }

    public void dispose()
    {
        shapeRenderer.dispose();
    }
}

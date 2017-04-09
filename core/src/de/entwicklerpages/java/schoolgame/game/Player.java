package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

import de.entwicklerpages.java.schoolgame.common.InputHelper;

public class Player {

    private static final float SPEED = 120f;
    private static final float ACCELERATION = 0.3f;
    private static final float RUN_FACTOR = 1.5f;
    private static final float DIAGONAL_SWITCH_TIME = 0.4f;
    private static final long LONG_ATTACK_TIME = 1700L;

    private int health;
    private String name;
    private boolean male;

    private float posX;
    private float posY;

    private float lastDeltaX = 0;
    private float lastDeltaY = 0;

    private float maxX;
    private float maxY;

    private long attackStart = -1;

    private EntityOrientation orientation;
    private float diagonalSwitchTimer = 0f;

    private SpriteBatch batch;
    private TextureAtlas playerAtlas;
    private TextureRegion playerFront;
    private TextureRegion playerSide;
    private TextureRegion playerBack;

    public Player(String name, boolean male) {
        this.health = 100;
        this.name = name;
        this.male = male;
        this.posX = 0;
        this.posY = 0;
        this.orientation = EntityOrientation.LOOK_FORWARD;

        String player = "player_" + (this.male ? "m" : "f");

        batch = new SpriteBatch();

        playerAtlas = new TextureAtlas(Gdx.files.internal("data/graphics/packed/" + player + ".atlas"));
        playerFront = playerAtlas.findRegion(player + "_front");
        playerSide = playerAtlas.findRegion(player + "_side");
        playerBack = playerAtlas.findRegion(player + "_back");
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

    public void setMaxMapDimension(float maxX, float maxY)
    {
        this.maxX = maxX;
        this.maxY = maxY;
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

        if (!MathUtils.isZero(deltaX) && !MathUtils.isZero(deltaY))
        {
            diagonalSwitchTimer += deltaTime;

            if (diagonalSwitchTimer <= 0)
            {
                deltaX = 0f;
            } else {
                deltaY = 0f;

                if (diagonalSwitchTimer >= DIAGONAL_SWITCH_TIME)
                {
                    diagonalSwitchTimer = -DIAGONAL_SWITCH_TIME;
                }
            }
        }

        if (!MathUtils.isZero(deltaX))
        {
            orientation = deltaX > 0 ? EntityOrientation.LOOK_RIGHT : EntityOrientation.LOOK_LEFT;
        }
        else if (!MathUtils.isZero(deltaY))
        {
            orientation = deltaY > 0 ? EntityOrientation.LOOK_BACKWARD : EntityOrientation.LOOK_FORWARD;
        }

        lastDeltaX = MathUtils.lerp(lastDeltaX, deltaX, ACCELERATION);
        lastDeltaY = MathUtils.lerp(lastDeltaY, deltaY, ACCELERATION);

        this.posX += lastDeltaX * deltaTime;
        this.posY += lastDeltaY * deltaTime;

        if (this.posX - playerSide.getRegionWidth() / 2 < 5) this.posX = playerSide.getRegionWidth() / 2 + 5;
        if (this.posY < 5) this.posY = 5;

        if (this.posX + playerSide.getRegionWidth() / 2 > maxX - 5) this.posX = maxX - playerSide.getRegionWidth() / 2 - 5;
        if (this.posY + playerSide.getRegionHeight() / 2 > maxY - 5) this.posY = maxY - playerSide.getRegionHeight() / 2 - 5;
    }

    public void render(OrthographicCamera camera, float deltaTime)
    {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        TextureRegion region = null;
        float scaleX = 1f;

        switch (orientation)
        {
            case LOOK_FORWARD:
                region = playerFront;
                break;
            case LOOK_LEFT:
                region = playerSide;
                scaleX = -1f;
                break;
            case LOOK_BACKWARD:
                region = playerBack;
                scaleX = -1f;
                break;
            case LOOK_RIGHT:
                region = playerSide;
                break;
        }

        batch.draw(region,                                      // TextureRegion (front, back, side)
                posX - playerSide.getRegionWidth() / 2,         // Offset to the X position (character center)
                posY,                                           // Y position is at the foots
                playerSide.getRegionWidth() / 2,                // Origin X (important for flipping)
                playerSide.getRegionHeight(),                   // Origin Y
                playerSide.getRegionWidth(),                    // Width
                playerSide.getRegionHeight(),                   // Height
                scaleX,                                         // Scale X (-1 to flip)
                1f,                                             // Scale Y
                0f);                                            // Rotation

        batch.end();
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
        playerAtlas.dispose();
        batch.dispose();
    }
}

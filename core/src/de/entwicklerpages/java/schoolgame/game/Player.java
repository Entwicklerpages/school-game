package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import de.entwicklerpages.java.schoolgame.common.InputHelper;

public class Player {

    private static final float SPEED = 3.25f;
    private static final float ACCELERATION = 0.28f;
    private static final float RUN_FACTOR = 2f;
    private static final float DIAGONAL_SWITCH_TIME = 0.4f;
    private static final float CAGE_WIDTH = 20f;
    private static final long LONG_ATTACK_TIME = 1700L;

    private Body playerBody;

    private int health;
    private String name;
    private boolean male;

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

    public Player(World world, String name, boolean male) {
        this.health = 100;
        this.name = name;
        this.male = male;
        this.orientation = EntityOrientation.LOOK_FORWARD;

        String player = "player_" + (this.male ? "m" : "f");

        batch = new SpriteBatch();

        playerAtlas = new TextureAtlas(Gdx.files.internal("data/graphics/packed/" + player + ".atlas"));
        playerFront = playerAtlas.findRegion(player + "_front");
        playerSide = playerAtlas.findRegion(player + "_side");
        playerBack = playerAtlas.findRegion(player + "_back");

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        playerBody = world.createBody(bodyDef);
        playerBody.setUserData(this);

        PolygonShape playerBox = Physics.createRectangle(32f, 20f, new Vector2(0f, 5f));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerBox;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = Physics.CATEGORY_PLAYER;
        fixtureDef.filter.maskBits = Physics.MASK_PLAYER;

        playerBody.createFixture(fixtureDef).setUserData(this);
        playerBox.dispose();
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
        return playerBody.getPosition().x * Physics.PPM;
    }


    public float getPosY() {
        return playerBody.getPosition().y * Physics.PPM;
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

        buildCage();
    }

    public void setPosition(float posX, float posY) {
        this.playerBody.setTransform(posX * Physics.MPP, posY * Physics.MPP, 0f);
    }

    private void buildCage()
    {
        if (maxX <= 32f || maxY <= 32f) return; // Zu klein für den Spieler

        BodyDef bodyWallDef = new BodyDef();
        bodyWallDef.type = BodyDef.BodyType.StaticBody;

        Body wallLeft = playerBody.getWorld().createBody(bodyWallDef);
        Body wallRight = playerBody.getWorld().createBody(bodyWallDef);
        Body wallTop = playerBody.getWorld().createBody(bodyWallDef);
        Body wallBottom = playerBody.getWorld().createBody(bodyWallDef);

        PolygonShape wallLeftBox = Physics.createRectangle(CAGE_WIDTH, maxY, new Vector2(-CAGE_WIDTH * 0.5f, maxY * 0.5f));
        PolygonShape wallRightBox = Physics.createRectangle(CAGE_WIDTH, maxY, new Vector2(maxX + CAGE_WIDTH * 0.5f, maxY * 0.5f));
        PolygonShape wallTopBox = Physics.createRectangle(maxX, CAGE_WIDTH, new Vector2(maxX * 0.5f, maxY + CAGE_WIDTH * 0.5f - 7f)); // -7f damit die Wand etwas weiter unten ist und der Spieler nicht so weit aus dem Bild raus ragt.
        PolygonShape wallBottomBox = Physics.createRectangle(maxX, CAGE_WIDTH, new Vector2(maxX * 0.5f, -CAGE_WIDTH * 0.5f));

        FixtureDef wallLeftFixture = new FixtureDef();
        wallLeftFixture.shape = wallLeftBox;
        wallLeftFixture.filter.categoryBits = Physics.CATEGORY_WORLD;
        wallLeftFixture.filter.maskBits = Physics.MASK_WORLD;

        FixtureDef wallRightFixture = new FixtureDef();
        wallRightFixture.shape = wallRightBox;
        wallRightFixture.filter.categoryBits = Physics.CATEGORY_WORLD;
        wallRightFixture.filter.maskBits = Physics.MASK_WORLD;

        FixtureDef wallTopFixture = new FixtureDef();
        wallTopFixture.shape = wallTopBox;
        wallTopFixture.filter.categoryBits = Physics.CATEGORY_WORLD;
        wallTopFixture.filter.maskBits = Physics.MASK_WORLD;

        FixtureDef wallBottomFixture = new FixtureDef();
        wallBottomFixture.shape = wallBottomBox;
        wallBottomFixture.filter.categoryBits = Physics.CATEGORY_WORLD;
        wallBottomFixture.filter.maskBits = Physics.MASK_WORLD;

        wallLeft.createFixture(wallLeftFixture);
        wallRight.createFixture(wallRightFixture);
        wallTop.createFixture(wallTopFixture);
        wallBottom.createFixture(wallBottomFixture);

        wallLeftBox.dispose();
        wallRightBox.dispose();
        wallTopBox.dispose();
        wallBottomBox.dispose();
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

            if (diagonalSwitchTimer < 0f)
            {
                deltaX = 0f;
            } else {
                deltaY = 0f;

                if (diagonalSwitchTimer >= DIAGONAL_SWITCH_TIME)
                {
                    diagonalSwitchTimer = -DIAGONAL_SWITCH_TIME;
                }
            }
        } else {
            diagonalSwitchTimer = 0f;
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

        playerBody.setLinearVelocity(lastDeltaX, lastDeltaY);
    }

    public void render(OrthographicCamera camera, float deltaTime)
    {
        Vector2 pos = playerBody.getPosition();
        pos.scl(Physics.PPM);

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
                pos.x - playerSide.getRegionWidth() / 2,        // Offset to the X position (character center)
                pos.y,                                          // Y position is at the foots
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
            // Attacke
            Gdx.app.log("INFO", "Start Attack");
            attackStart = TimeUtils.millis();
            return true;
        }
        else if (InputHelper.checkKeys(keycode, Input.Keys.E, Input.Keys.ENTER))
        {
            // Interaktion
            Gdx.app.log("INFO", "Interaction");
            return true;
        }

        return false;
    }

    public boolean keyUp(int keycode)
    {
        if (keycode == Input.Keys.SPACE && attackStart > 0)
        {
            // Attacke

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

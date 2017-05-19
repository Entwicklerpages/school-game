package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import de.entwicklerpages.java.schoolgame.AudioManager;
import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.common.InputManager;

/**
 * Spielerklasse.
 * Ist für die Steuerung und Darstellung des Spielercharakters zuständig.
 *
 * @author nico
 */
public class Player implements ExtendedMapDisplayObject {

    private static final float SPEED = 3.25f;
    private static final float ACCELERATION = 0.28f;
    private static final float RUN_FACTOR = 2f;
    private static final float DIAGONAL_SWITCH_TIME = 0.4f;
    private static final float CAGE_THICKNESS = 20f;
    private static final long LONG_ATTACK_TIME = 1100L;
    //private static final float SOUND_PAUSE = 0.2f;

    private final Body playerBody;
    private final CheatManager cheatManager;

    private int health;
    private final String name;
    private final boolean male;

    private float lastDeltaX = 0;
    private float lastDeltaY = 0;

    private boolean cageBuilt = false;

    private long attackStart = -1;
    private float attackSword = -1f;

    private EntityOrientation orientation;
    private float diagonalSwitchTimer = 0f;
    //private float soundTimer = SOUND_PAUSE;
    //private boolean stepSide = false;

    private final TextureAtlas playerAtlas;
    private final TextureRegion playerFront;
    private final TextureRegion playerSide;
    private final TextureRegion playerBack;

    private final Animation<TextureRegion> playerFrontWalk;
    private final Animation<TextureRegion> playerSideWalk;
    private final Animation<TextureRegion> playerBackWalk;

    private final Array<TextureAtlas.AtlasRegion> playerAttack;

    /*private final AudioManager audioManager;
    private final AudioManager.SoundKey leftStep;
    private final AudioManager.SoundKey rightStep;*/

    private final SpriteBatch interfaceBatch;
    private final ShapeRenderer interfaceRenderer;
    private final BitmapFont interfaceFont;
    private final GlyphLayout fontLayout;
    private final Matrix4 interfaceProjection;

    private ActionCallback deadCallback = null;
    private ActionCallback interactionCallback = null;
    private WorldObjectManager.AttackCallback attackCallback = null;

    private float animationTime = 0f;
    private float damageTime = 0f;

    /**
     * Initialisierung.
     *
     * @param world Zugriff auf die Box2D Welt
     * @param name der Name des Spielers
     * @param male ist der Spieler männlich oder weiblich?
     */
    public Player(SchoolGame game, World world, String name, boolean male) {
        this.health = 100;
        this.name = name;
        this.male = male;
        this.orientation = EntityOrientation.LOOK_FORWARD;

        this.cheatManager = CheatManager.getInstance();

        String player = "player_" + (this.male ? "m" : "f");

        playerAtlas = new TextureAtlas(Gdx.files.internal("data/graphics/packed/" + player + ".atlas"));
        playerFront = playerAtlas.findRegion(player + "_front");
        playerSide = playerAtlas.findRegion(player + "_side");
        playerBack = playerAtlas.findRegion(player + "_back");

        playerFrontWalk = new Animation<TextureRegion>(1/7f, playerAtlas.findRegions(player + "_front_walk"), Animation.PlayMode.LOOP);
        playerSideWalk = new Animation<TextureRegion>(1/7f, playerAtlas.findRegions(player + "_side_walk"), Animation.PlayMode.LOOP);
        playerBackWalk = new Animation<TextureRegion>(1/7f, playerAtlas.findRegions(player + "_back_walk"), Animation.PlayMode.LOOP);

        playerAttack = playerAtlas.findRegions(player + "_sword");

        /*audioManager = game.getAudioManager();
        leftStep = audioManager.createSound("walk", "grass_left.wav", true);
        rightStep = audioManager.createSound("walk", "grass_right.wav", true);*/

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


        interfaceBatch = new SpriteBatch();
        interfaceRenderer = new ShapeRenderer();
        interfaceFont = game.getLongTextFont();
        fontLayout = new GlyphLayout();
        interfaceProjection = new Matrix4();
    }

    /**
     * Ruft die Anzahl der Lebenspunkte ab.
     *
     * @return die Anzahl der Lebenspunkte
     */
    public int getHealth() {
        return health;
    }

    /**
     * Setzt die Anzahl der Lebenspunkte direkt.
     * Sollte so ehr selten benutzt werden.
     *
     * @see Player#applyDamage(int)
     * @see Player#heal(int)
     *
     * @param health die neue Anzahl Lebenspunkte
     */
    public void setHealth(int health) {
        if (health > 100)
            health = 100;

        if (health < 0 || MathUtils.isZero(health))
        {
            if (cheatManager.isImmortal())
            {
                this.health = 100;
                return;
            }

            health = 0;
            if (deadCallback != null)
                deadCallback.run();
        }

        this.health = health;
    }

    /**
     * Legt fest, welches Callback aufgerufen werden soll,
     * wenn der Spieler stirbt.
     *
     * @param deadCallback das Callback
     */
    public void setDeadCallback(ActionCallback deadCallback)
    {
        this.deadCallback = deadCallback;
    }

    /**
     * Legt fest, welches Callback aufgerufen werden soll,
     * wenn der Spieler die Interaktionstaste drückt.
     *
     * @param interactionCallback das Callback
     */
    public void setInteractionCallback(ActionCallback interactionCallback)
    {
        this.interactionCallback = interactionCallback;
    }

    /**
     * Legt fest, welches Callback aufgerufen werden soll,
     * wenn der Spieler einen Angriff durchführt.
     *
     * @param attackCallback das Callback
     */
    public void setAttackCallback(WorldObjectManager.AttackCallback attackCallback)
    {
        this.attackCallback = attackCallback;
    }

    /**
     * Fügt dem Spieler schaden zu.
     *
     * @param damage wie viele Lebenspunkte soll der Spieler verlieren?
     */
    public void applyDamage(int damage) {

        if (!MathUtils.isZero(damageTime)) return;

        if (!cheatManager.isImmortal())
            this.setHealth(health - damage);

        damageTime = 0.1f * (float) damage;
    }

    /**
     * Heilt den Spieler.
     *
     * @param healAmount wie viele Lebenspunkte soll der Spieler bekommen?
     */
    public void heal(int healAmount) {
        this.setHealth(health + healAmount);
    }

    /**
     * Gibt den Namen des Spielers zurück.
     *
     * @return der Name des Spielers
     */
    public String getName() {
        return name;
    }

    /**
     * Ruft ab, ob der Spieler männlich oder weiblich ist.
     *
     * @return true wenn männlich, false wenn weiblich
     */
    public boolean isMale() {
        return male;
    }

    /**
     * Gibt die X Position des Spielers zurück.
     *
     * @return die X Position
     */
    public float getPosX() {
        return playerBody.getPosition().x * Physics.PPM;
    }

    /**
     * Gibt die Y Position des Spielers zurück.
     *
     * @return die Y Position
     */
    public float getPosY() {
        return playerBody.getPosition().y * Physics.PPM;
    }

    /**
     * Ruft ab, in welche Richtung der Spieler gerade sieht.
     *
     * @return die Richtung, in die der Spieler sieht.
     */
    public EntityOrientation getOrientation() {
        return orientation;
    }

    /**
     * Legt fest, in welche Richtung der Spieler sieht.
     *
     * @param orientation die neue Richtung
     */
    public void setOrientation(EntityOrientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Baut einen "Käfig" um die Map, sofern nicht vorher schon eine Größe übergeben wurde.
     *
     * @see Player#buildCage(float, float)
     *
     * @param maxX die Breite der Map
     * @param maxY die Höhe der Map
     */
    public void setMaxMapDimension(float maxX, float maxY)
    {
        if (!cageBuilt)
        {
            buildCage(maxX, maxY);
            cageBuilt = true;
        }
    }

    /**
     * Verschiebt den Spieler an eine andere Position.
     *
     * @param posX die neue X Position
     * @param posY die neue Y Position
     */
    public void setPosition(float posX, float posY) {
        this.playerBody.setTransform(posX * Physics.MPP, posY * Physics.MPP, 0f);
    }

    /**
     * Erstellt 4 Box2D Körper, die verhindern, das die Spielfigur die Map verlässt.
     */
    private void buildCage(float maxX, float maxY)
    {
        if (maxX <= 32f || maxY <= 32f) return; // Zu klein für den Spieler

        BodyDef bodyWallDef = new BodyDef();
        bodyWallDef.type = BodyDef.BodyType.StaticBody;

        Body wallLeft = playerBody.getWorld().createBody(bodyWallDef);
        Body wallRight = playerBody.getWorld().createBody(bodyWallDef);
        Body wallTop = playerBody.getWorld().createBody(bodyWallDef);
        Body wallBottom = playerBody.getWorld().createBody(bodyWallDef);

        PolygonShape wallLeftBox = Physics.createRectangle(CAGE_THICKNESS, maxY, new Vector2(-CAGE_THICKNESS * 0.5f, maxY * 0.5f));
        PolygonShape wallRightBox = Physics.createRectangle(CAGE_THICKNESS, maxY, new Vector2(maxX + CAGE_THICKNESS * 0.5f, maxY * 0.5f));
        PolygonShape wallTopBox = Physics.createRectangle(maxX, CAGE_THICKNESS, new Vector2(maxX * 0.5f, maxY + CAGE_THICKNESS * 0.5f - 7f)); // -7f damit die Wand etwas weiter unten ist und der Spieler nicht so weit aus dem Bild raus ragt.
        PolygonShape wallBottomBox = Physics.createRectangle(maxX, CAGE_THICKNESS, new Vector2(maxX * 0.5f, -CAGE_THICKNESS * 0.5f));

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

    /**
     * Wird jeden Frame aufgerufen um eine Bewegung des Spielers durchzuführen.
     *
     * @param deltaTime die Zeit, die seit dem letzten Frame vergangen ist
     */
    public void update(float deltaTime) {
        float deltaX = 0;
        float deltaY = 0;

        if (!MathUtils.isZero(damageTime))
        {
            damageTime -= deltaTime;

            if (damageTime <= 0.0f)
                damageTime = 0f;
        }

        if (InputManager.checkActionActive(InputManager.Action.MOVE_UP))
        {
            deltaY += SPEED;
        }

        if (InputManager.checkActionActive(InputManager.Action.MOVE_LEFT))
        {
            deltaX -= SPEED;
        }

        if (InputManager.checkActionActive(InputManager.Action.MOVE_DOWN))
        {
            deltaY -= SPEED;
        }

        if (InputManager.checkActionActive(InputManager.Action.MOVE_RIGHT))
        {
            deltaX += SPEED;
        }

        if (InputManager.checkActionActive(InputManager.Action.RUN))
        {
            deltaX *= RUN_FACTOR;
            deltaY *= RUN_FACTOR;
        }

        if (cheatManager.isSuperFast())
        {
            deltaX *= 2.4f;
            deltaY *= 2.4f;
        }

        if (attackStart != -1)
        {
            deltaX = 0f;
            deltaY = 0f;
        }

        /*if (!MathUtils.isZero(deltaX) || !MathUtils.isZero(deltaY))
        {
            soundTimer -= deltaTime;

            if (soundTimer <= 0f)
            {
                soundTimer = SOUND_PAUSE;

                audioManager.playSound(stepSide ? leftStep : rightStep, 0.3f);
                stepSide = !stepSide;
            }
        }*/

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

    /**
     * Zeigt die Spielfigur an. Dabei wird die Ausrichtung und der Zustand der Figur beachtet.
     *
     * Zustände: Stehen, Laufen, Attacke
     *
     * @param batch der Batch, in den gerendert werden soll
     * @param deltaTime die Zeit, die seit dem letztem Frame vergangen ist
     */
    public void render(Batch batch, float deltaTime)
    {
        Vector2 pos = playerBody.getPosition();
        pos.scl(Physics.PPM);

        TextureRegion region = null;
        float scaleX = 1f;
        boolean notMoving = MathUtils.isZero(lastDeltaX, 0.5f) && MathUtils.isZero(lastDeltaY, 0.5f);

        if (notMoving)
        {
            animationTime = 0f;
        } else {
            animationTime += deltaTime;
        }

        if (attackStart == -1 && attackSword <= 0)
        {
            switch (orientation)
            {
                case LOOK_FORWARD:
                    region = notMoving ? playerFront : playerFrontWalk.getKeyFrame(animationTime);
                    break;
                case LOOK_LEFT:
                    region = notMoving ? playerSide : playerSideWalk.getKeyFrame(animationTime);
                    scaleX = -1f;
                    break;
                case LOOK_BACKWARD:
                    region = notMoving ? playerBack : playerBackWalk.getKeyFrame(animationTime);
                    scaleX = -1f;
                    break;
                case LOOK_RIGHT:
                    region = notMoving ? playerSide : playerSideWalk.getKeyFrame(animationTime);
                    break;
            }
        } else {
            switch (orientation)
            {
                case LOOK_LEFT:
                case LOOK_BACKWARD:
                    scaleX = -1f;
                    break;
            }
            if (attackSword > 0f)
            {
                attackSword -= deltaTime;
                if (attackSword <= 0f)
                    attackSword = -1f;

                region = playerAttack.get(2);
            }
            else
            {
                region = playerAttack.get(1);
            }
        }

        if (!MathUtils.isZero(damageTime))
        {
            if (damageTime % 0.4f > 0.2f)
            {
                Color oldColor = batch.getColor();
                batch.setColor(Color.RED);

                batch.draw(region,                                  // TextureRegion (front, back, side)
                        pos.x - region.getRegionWidth() / 2,        // Offset to the X position (character center)
                        pos.y,                                      // Y position is at the foots
                        region.getRegionWidth() / 2,                // Origin X (important for flipping)
                        region.getRegionHeight(),                   // Origin Y
                        region.getRegionWidth(),                    // Width
                        region.getRegionHeight(),                   // Height
                        scaleX,                                     // Scale X (-1 to flip)
                        1f,                                         // Scale Y
                        0f);

                batch.setColor(oldColor);
            }
        }
        else
        {
            batch.draw(region,                                  // TextureRegion (front, back, side)
                    pos.x - region.getRegionWidth() / 2,        // Offset to the X position (character center)
                    pos.y,                                      // Y position is at the foots
                    region.getRegionWidth() / 2,                // Origin X (important for flipping)
                    region.getRegionHeight(),                   // Origin Y
                    region.getRegionWidth(),                    // Width
                    region.getRegionHeight(),                   // Height
                    scaleX,                                     // Scale X (-1 to flip)
                    1f,                                         // Scale Y
                    0f);                                        // Rotation
        }
    }

    /**
     * Zeigt das Interface mit Lebensstand an.
     *
     * @param camera Zugriff auf die Kamera
     * @param interaction true, wenn eine Interaktion möglich ist
     */
    public void renderInterface(OrthographicCamera camera, boolean interaction)
    {
        interfaceProjection.set(camera.projection).translate(-camera.viewportWidth / 2f, -camera.viewportHeight / 2f, 0f);

        interfaceRenderer.setProjectionMatrix(interfaceProjection);

        ////////////////////

        interfaceRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float healthWidth = (((float)health) / 100f) * 200f;

        interfaceRenderer.setColor(Color.FIREBRICK);
        interfaceRenderer.rect(20, camera.viewportHeight - 50, healthWidth, 20);

        interfaceRenderer.end();

        ////////////////////

        interfaceRenderer.begin(ShapeRenderer.ShapeType.Line);

        interfaceRenderer.setColor(Color.WHITE);
        interfaceRenderer.rect(20, camera.viewportHeight - 50, 200, 20);

        interfaceRenderer.end();

        ////////////////////

        if (interaction)
        {
            interfaceBatch.setProjectionMatrix(interfaceProjection);
            interfaceBatch.begin();

            fontLayout.setText(interfaceFont, "E / Enter zum Interagieren", Color.WHITE, camera.viewportWidth, Align.center, false);
            interfaceFont.draw(interfaceBatch, fontLayout, 0,  40);

            interfaceBatch.end();
        }
    }

    /**
     * Regiert auf das Drücken einer Taste.
     *
     * Startet eine Attacke oder führt eine Interaktion durch.
     *
     * @param keycode der Tastencode der Taste
     * @return true, wenn auf das Ereignis reagiert wurde
     */
    public boolean keyDown(int keycode)
    {
        InputManager.Action action = InputManager.checkGameAction(keycode);

        if (action == InputManager.Action.ATTACK && attackSword <= 0f)
        {
            // Attacke
            attackStart = TimeUtils.millis();
            return true;
        }
        else if (action == InputManager.Action.INTERACTION && attackStart == -1)
        {
            // Interaktion
            if (interactionCallback != null)
                interactionCallback.run();

            return true;
        }
        else if (cheatManager.isHealthControlled() && action == InputManager.Action.CHEAT_DAMAGE)
        {
            applyDamage(10);
            return true;
        }
        else if (cheatManager.isHealthControlled() && action == InputManager.Action.CHEAT_HEAL)
        {
            heal(15);
            return true;
        }

        return false;
    }

    /**
     * Regiert auf das Loslassen einer Taste.
     *
     * Führt die Attacke nach dem Loslassen der entsprechenden Taste durch.
     *
     * @param keycode der Tastencode der Taste
     * @return true, wenn auf das Ereignis reagiert wurde
     */
    public boolean keyUp(int keycode)
    {
        InputManager.Action action = InputManager.checkGameAction(keycode);

        if (action == InputManager.Action.ATTACK && attackStart > 0)
        {
            // Attacke

            if (attackCallback == null)
                return false;

            long attackTime = TimeUtils.timeSinceMillis(attackStart);
            if (attackTime >= LONG_ATTACK_TIME)
            {
                attackCallback.run((int)(attackTime/400));
                attackSword = 0.4f;
            } else {
                attackCallback.run(1);
                attackSword = 0.2f;
            }

            attackStart = -1;
            return true;
        }
        return false;
    }

    /**
     * Aufräumarbeiten.
     */
    public void dispose()
    {
        playerAtlas.dispose();
        interfaceRenderer.dispose();
        interfaceBatch.dispose();
    }
}

package de.entwicklerpages.java.schoolgame.game.objects.entities.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.entwicklerpages.java.schoolgame.game.Physics;
import de.entwicklerpages.java.schoolgame.game.objects.entities.BaseEntity;

/**
 * Basisklasse für NPCs
 *
 * @author nico
 */
public abstract class BaseNPC extends BaseEntity
{
    protected String baseName;

    protected Body body;

    private TextureAtlas npcAtlas;
    private TextureRegion npcFront = null;
    private TextureRegion npcSide = null;
    private TextureRegion npcBack = null;

    private Animation<TextureRegion> npcFrontWalk = null;
    private Animation<TextureRegion> npcSideWalk = null;
    private Animation<TextureRegion> npcBackWalk = null;

    private float animationTime = 0f;

    public BaseNPC(String objectId, String baseName)
    {
        super(objectId);
        this.baseName = baseName;
    }

    @Override
    public float getPosY()
    {
        if (body != null)
            return body.getPosition().y * Physics.PPM;
        else
            return super.getPosY();
    }

    @Override
    public void onInit()
    {
        super.onInit();

        npcAtlas = new TextureAtlas(Gdx.files.internal("data/graphics/packed/" + baseName + ".atlas"));
        npcFront = npcAtlas.findRegion(baseName + "_front");
        npcSide = npcAtlas.findRegion(baseName + "_side");
        npcBack = npcAtlas.findRegion(baseName + "_back");

        npcFrontWalk = new Animation<TextureRegion>(1/5f, npcAtlas.findRegions(baseName + "_front_walk"), Animation.PlayMode.LOOP);
        npcSideWalk = new Animation<TextureRegion>(1/5f, npcAtlas.findRegions(baseName + "_side_walk"), Animation.PlayMode.LOOP);
        npcBackWalk = new Animation<TextureRegion>(1/5f, npcAtlas.findRegions(baseName + "_back_walk"), Animation.PlayMode.LOOP);
    }

    @Override
    public void render(Batch batch, float deltaTime)
    {
        if (body == null) return;

        Vector2 pos = body.getPosition();
        pos.scl(Physics.PPM);

        TextureRegion region = null;
        float scaleX = 1f;

        boolean notMoving = MathUtils.isZero(body.getLinearVelocity().x, 0.5f) && MathUtils.isZero(body.getLinearVelocity().y, 0.5f);

        if (notMoving)
        {
            animationTime = 0f;
        } else {
            animationTime += deltaTime;
        }

        switch (orientation)
        {
            case LOOK_FORWARD:
                region = notMoving ? npcFront : npcFrontWalk.getKeyFrame(animationTime);
                break;
            case LOOK_LEFT:
                region = notMoving ? npcSide : npcSideWalk.getKeyFrame(animationTime);
                scaleX = -1f;
                break;
            case LOOK_BACKWARD:
                region = notMoving ? npcBack : npcBackWalk.getKeyFrame(animationTime);
                scaleX = -1f;
                break;
            case LOOK_RIGHT:
                region = notMoving ? npcSide : npcSideWalk.getKeyFrame(animationTime);
                break;
        }

        batch.draw(region,                                      // TextureRegion (front, back, side)
                pos.x - npcSide.getRegionWidth() / 2,           // Offset to the X position (character center)
                pos.y,                                          // Y position is at the foots
                npcSide.getRegionWidth() / 2,                   // Origin X (important for flipping)
                npcSide.getRegionHeight(),                      // Origin Y
                npcSide.getRegionWidth(),                       // Width
                npcSide.getRegionHeight(),                      // Height
                scaleX,                                         // Scale X (-1 to flip)
                1f,                                             // Scale Y
                0f);                                            // Rotation
    }

    @Override
    public void onDispose()
    {
        super.onDispose();
        npcAtlas.dispose();
    }
}

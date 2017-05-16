package de.entwicklerpages.java.schoolgame.game.objects;

/**
 * Angriffszone.
 *
 * Fügt dem Spieler dauerhaft Schaden zu.
 */
public class AttackZone extends WorldObject implements UpdateObject
{
    private int damage;
    private float timeout = 3f;
    private AttackHandler handler;
    private float timer = 0f;

    /**
     * Konstruktor.
     *
     * @param objectId die ID des MapObjects
     */
    public AttackZone(String objectId)
    {
        this(objectId, 0);
    }

    /**
     * Konstruktor.
     *
     * @param objectId die ID des MapObjects
     * @param damage der Schaden, er dem Spieler zugefügt wird.
     */
    public AttackZone(String objectId, int damage)
    {
        super(objectId);

        this.damage = damage;
    }

    /**
     * Initialisierung
     */
    @Override
    public void onInit()
    {
        handler = new AttackHandler(worldObjectManager);
        handler.createStaticAttackBody(rawObject);
    }

    /**
     * Legt einen neuen Schaden fest.
     *
     * @param damage der Schaden
     */
    public void setDamage(int damage)
    {
        this.damage = damage;
    }

    /**
     * Legt einen neuen Timeout fest.
     *
     * @param timeout der neue Timeout
     */
    public void setTimeout(float timeout)
    {
        this.timeout = timeout;
    }

    @Override
    public void onUpdate(float deltaTime)
    {
        if (timer > 0f)
            timer -= deltaTime;

        if (timer <= 0f && handler.canAttack())
        {
            handler.attack(damage);
            timer = timeout;
        }
    }

    @Override
    public void onDispose()
    {
        handler.dispose();
    }
}

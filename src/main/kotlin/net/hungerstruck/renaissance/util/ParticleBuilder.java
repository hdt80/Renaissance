package net.hungerstruck.renaissance.util;

import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleBuilder {

    private Particle particle = Particle.FLAME;
    private boolean longDistance = false;
    private Location location = null;
    private float offsetX = 0, offsetY = 0, offsetZ = 0, speed = 0;
    private int count = 0;
    private int[] data = new int[0];

    /**
     * Gets the current set particle
     *
     * @return
     */
    public Particle getParticle() {
        return particle;
    }

    /**
     * Sets a new particle
     *
     * @param particle
     * @return
     */
    public ParticleBuilder setParticle(Particle particle) {
        this.particle = particle;
        return this;
    }

    /**
     * Can it be seen from further then 256 blocks?
     *
     * @return
     */
    public boolean isLongDistance() {
        return longDistance;
    }

    /**
     * Set if it can be seen from further then 256 blocks
     *
     * @param longDistance
     * @return
     */
    public ParticleBuilder setLongDistance(boolean longDistance) {
        this.longDistance = longDistance;
        return this;
    }

    /**
     * Get where the particle will play
     *
     * @return
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Set where the particle will play
     *
     * @param location
     * @return
     */
    public ParticleBuilder setLocation(Location location) {
        this.location = location;
        return this;
    }

    /**
     * Sets offset for X, Y, Z
     *
     * @param offsetX
     * @param offsetY
     * @param offsetZ
     * @return
     */
    public ParticleBuilder setOffset(float offsetX, float offsetY, float offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        return this;
    }

    /**
     * Sets offset for X, Y, Z
     *
     * @param offset
     * @return
     */
    public ParticleBuilder setOffset(float offset) {
        return setOffset(offset, offset, offset);
    }

    /**
     * Gets offset for X
     *
     * @return
     */
    public float getOffsetX() {
        return offsetX;
    }

    /**
     * Sets offset for X
     *
     * @param offsetX
     * @return
     */
    public ParticleBuilder setOffsetX(float offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    /**
     * Gets offset for Y
     *
     * @return
     */
    public float getOffsetY() {
        return offsetY;
    }

    /**
     * Sets offset for Y
     *
     * @param offsetY
     * @return
     */
    public ParticleBuilder setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    /**
     * Gets offset for Z
     *
     * @return
     */
    public float getOffsetZ() {
        return offsetZ;
    }

    /**
     * Sets offset for Z
     *
     * @param offsetZ
     * @return
     */
    public ParticleBuilder setOffsetZ(float offsetZ) {
        this.offsetZ = offsetZ;
        return this;
    }

    /**
     * Gets particle speed
     *
     * @return
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Sets particle speed
     *
     * @param speed
     * @return
     */
    public ParticleBuilder setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    /**
     * Gets particle count
     *
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets particle count
     *
     * @param count
     * @return
     */
    public ParticleBuilder setCount(int count) {
        this.count = count;
        return this;
    }

    /**
     * Gets particle data
     *
     * @return
     */
    public int[] getData() {
        return data;
    }

    /**
     * Sets particle data
     *
     * @param data
     * @return
     */
    public ParticleBuilder setData(int... data) {
        this.data = data;
        return this;
    }
}

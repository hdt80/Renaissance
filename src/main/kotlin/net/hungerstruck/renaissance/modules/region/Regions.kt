package net.hungerstruck.renaissance.modules.region

import net.hungerstruck.renaissance.RLogger
import net.hungerstruck.renaissance.between
import net.hungerstruck.renaissance.pow
import org.bukkit.util.Vector
import java.security.SecureRandom
import java.util.concurrent.ThreadLocalRandom
import javax.naming.OperationNotSupportedException

/**
 * Main parent interface for all regions.
 */
interface RRegion {
    fun contains(point: Vector): Boolean

    fun randomPoint(): Vector
}

data class BlockRegion(val loc: Vector) : RRegion {
    override fun contains(point: Vector) = loc == point

    override fun randomPoint(): Vector {
        return loc
    }
}

data class CircleRegion(val baseX: Double, val baseZ: Double, val radius: Int) : RRegion {
    override fun contains(point: Vector) = ((point.x - baseX) pow 2.0) + ((point.z - baseZ) pow 2.0) <= radius * radius

    override fun randomPoint(): Vector {
        val randRad = SecureRandom().nextInt(radius)
        return Vector(randRad * baseX, 0.toDouble(), (radius - randRad) * baseZ)
    }
}

data class CuboidRegion(val min: Vector, val max: Vector) : RRegion {
    override fun contains(point: Vector) = point.isInAABB(min, max)

    override fun randomPoint(): Vector {
        return Vector(ThreadLocalRandom.current().nextInt(min.blockX, max.blockX + 1),
                ThreadLocalRandom.current().nextInt(min.blockY, max.blockY + 1),
                ThreadLocalRandom.current().nextInt(min.blockZ, max.blockZ + 1))
    }
}

data class CylinderRegion(val base: Vector, val radius: Int, val height: Int) : RRegion {
    override fun contains(point: Vector): Boolean {
        if (point.y < this.base.y || point.y > (this.base.y + this.height))
            return false
        return Math.pow(point.x - base.x, 2.0) + Math.pow(point.z - base.z, 2.0) <= (radius * radius)
    }

    override fun randomPoint(): Vector {
        val randRad = SecureRandom().nextInt(radius)
        return Vector(randRad * base.blockX, base.blockY + SecureRandom().nextInt(height), (radius - randRad) * base.blockZ)
    }
}

data class IntersectRegion(val regions: List<RRegion>) : RRegion {
    override fun contains(point: Vector) = regions.all { it.contains(point) }

    override fun randomPoint(): Vector {
        throw OperationNotSupportedException("Cannot get a random point from a region intersection")
    }
}

data class InvertedRegion(val region: RRegion) : RRegion {
    override fun contains(point: Vector) = !region.contains(point)

    override fun randomPoint(): Vector {
        throw OperationNotSupportedException("Cannot get a random point from an inverted region")
    }
}

data class RectangleRegion(val min: Vector, val max: Vector) : RRegion {
    override fun contains(point: Vector) = point.x.between(min.x, max.x) && point.z.between(min.z, max.z)

    override fun randomPoint(): Vector {
        return Vector(ThreadLocalRandom.current().nextInt(min.blockX, max.blockX + 1),
                ThreadLocalRandom.current().nextInt(min.blockY, max.blockY + 1),
                ThreadLocalRandom.current().nextInt(min.blockZ, max.blockZ + 1))
    }
}

data class SphereRegion(val center: Vector, val radius: Int) : RRegion {
    override fun contains(point: Vector) = point.isInSphere(center, radius.toDouble())

    override fun randomPoint(): Vector {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

data class UnionRegion(val regions: List<RRegion>) : RRegion {
    override fun contains(point: Vector) = regions.any { it.contains(point) }

    override fun randomPoint(): Vector {
        throw OperationNotSupportedException("Cannot get a random point from a region union")
    }
}
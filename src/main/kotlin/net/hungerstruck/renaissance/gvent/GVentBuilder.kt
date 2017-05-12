package net.hungerstruck.renaissance.gvent

import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.modules.region.*
import org.bukkit.util.Vector

class GVentBuilder : AbstractGVentBuilder<GVentBuilder>() {

    lateinit var name: String
    lateinit var desc: String

    lateinit var executeFun: (RMatch) -> Unit

    /**
     * Region building
     */

    fun block(loc: Vector) = BlockRegion(loc)
    fun circle(base: IntRange, radius: Int) = CircleRegion(base.first.toDouble(), base.last.toDouble(), radius)
    fun cuboid(min: Vector, max: Vector) = CuboidRegion(min, max)
    fun cylinder(base: Vector, radius: Int, height: Int) = CylinderRegion(base, radius, height)
    fun rectangle(min: IntRange, max: IntRange) = RectangleRegion(Vector(min.first, 0, min.last), Vector(max.first, 0, max.last))
    fun sphere(center: Vector, radius: Int) = SphereRegion(center, radius)
}

operator fun IntRange.rangeTo(other: Int): Vector {
    return Vector(first, last, other)
}

fun gvent(x: GVentBuilder.() -> Unit): GVentBuilder {
    val builder = GVentBuilder()
    builder.x()
    return builder
}


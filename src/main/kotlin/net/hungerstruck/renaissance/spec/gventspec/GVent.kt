package net.hungerstruck.renaissance.spec.gventspec

import java.io.File

/**
 * GVent is a Game Event, which is not a Bukkit event. A Bukkit event is when an action happens, while a GVent is a
 *      series of actions, similar to what a Game Master from Hunger Games would do.
 *
 * There are two types of gvents
 *      1. Instant gvent
 *          An instant event only does one action, and does not perform a continuous action. These types of events would
 *          be used to keep a map moving along.
 *
 *          Triggers:
 *              Ran by a command
 *              A specific event in the map occurred
 *              After a period of time
 *
 *          Examples:
 *              Spawn Ghasts in the sky
 *              Turn all ores to TNT
 *
 *      2. Continuous gvent
 *          A continuous event will be present for the rest of the map. They will alter the way a map is played.
 *
 *          Triggers:
 *              Ran by a command
 *              A specific event in the map occurred
 *
 *          Examples:
 *              Lava slowly rising from the bottom of the map
 *              Mobs spawn after a chest is opened
 *
 * @param loc File location where the event is located at
 */
class GVent(loc: File) {
    var gventBuilder: GVentBuilder
    var gventInfo: GVentInfo

    val fileLocation: File = loc

    init {
        this.gventBuilder = loadGVentBuilder()
        this.gventInfo = loadGVentInfo()
    }

    private fun loadGVentBuilder(): GVentBuilder {
        val loader = GVentBuilderClassLoader(this.fileLocation)

        val gventClass: Class<*> = loader.loadClass("GVent")
        val gventInstance: Any = gventClass.newInstance()

        for (method in gventClass.declaredMethods) {
            if (method.name == "gvent") {
                method.isAccessible = true
                return method.invoke(gventInstance) as GVentBuilder
            }
        }

        throw RuntimeException("GVent builder has no method gvent()")
    }

    private fun loadGVentInfo(): GVentInfo {
        return GVentInfo(gventBuilder.name, gventBuilder.desc)
    }
}
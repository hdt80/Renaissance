package net.hungerstruck.renaissance.spec.gventspec

import com.google.common.collect.ImmutableList
import net.hungerstruck.renaissance.RLogger
import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.util.LiquidMetal
import java.io.File

class GVentContext {
    private val gvents: MutableMap<String, GVent> = hashMapOf()

    fun getGVentExact(name: String) = gvents[name]
    fun getGVents() = ImmutableList.copyOf(gvents.values)

    fun loadGVents(directory: File) {
        if (!directory.exists() || !directory.isDirectory) throw IllegalArgumentException("Illegal gvent path: ${directory.absolutePath}")

        RLogger.debug("===== Loading GVents =====")

        for (f in directory.listFiles()) {
            if (!f.isDirectory) continue

            if (File(f, RConfig.GVents.gventFileName).exists()) {
                val gvent = GVent(f)
                gvents.put(gvent.gventInfo.name, gvent)

                RLogger.debug("Loaded gvent: ${gvent.gventInfo.name}")
            }
        }
    }

    fun unloadGVents() {
        gvents.clear()
    }

    fun matchMap(query: String): GVent? {
        return LiquidMetal.fuzzyMatch(getGVents(), query, { it.gventInfo.name }, 0.9)
    }
}

package net.hungerstruck.renaissance.spec.mapspec

import com.google.common.collect.ImmutableList
import net.hungerstruck.renaissance.RLogger
import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.util.LiquidMetal
import java.io.File

class RMapContext {
    private val maps: MutableMap<String, RMap> = hashMapOf()

    fun getMapExact(name: String) = maps[name]
    fun getMaps() = ImmutableList.copyOf(maps.values)

    fun loadMaps(directory: File) {
        if (!directory.exists() || !directory.isDirectory) throw IllegalArgumentException("Illegal map path: ${directory.absolutePath}")

        RLogger.debug("===== Loading maps =====")
        for (f in directory.listFiles()) {
            if (!f.isDirectory) continue

            if (File(f, RConfig.Maps.mapFileName).exists()) {
                RLogger.debug("Loading map from ${f.name}")
                val map = RMap(f)
                maps.put(map.mapInfo.name, map)
            }
        }
    }

    fun matchMap(query: String): RMap? {
        return LiquidMetal.fuzzyMatch(getMaps(), query, { it.mapInfo.name }, 0.9)
    }
}
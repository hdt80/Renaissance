package net.hungerstruck.renaissance.spec.mapspec

import java.io.File
import java.security.SecureRandom

/**
 * Represents a map.
 */
class RMap(loc: File) {

    var mapBuilder: MapBuilder
    var mapInfo: RMapInfo

    val location: java.io.File = loc
    val isLobby: Boolean
        get() = this.mapInfo.lobbyProperties != null

    init {
        this.mapBuilder = this.loadMapBuilder()
        this.mapInfo = loadMapInfo()
    }

    private fun loadMapBuilder(): MapBuilder {
        val loader = MapBuilderClassLoader(this.location)

        val mapClass: Class<*> = loader.loadClass("Map")
        val mapInstance: Any = mapClass.newInstance()

        for (method in mapClass.declaredMethods) {
            if (method.name == "map") {
                method.isAccessible = true
                return method.invoke(mapInstance) as MapBuilder
            }
        }

        throw RuntimeException("Map builder has no method map()")
    }

    private fun loadMapInfo(): RMapInfo {
        return RMapInfo(
                name = mapBuilder.name,
                version = mapBuilder.version,
                objective = mapBuilder.objective,
                authors = mapBuilder.authors,
                contributors = mapBuilder.contributors,
                rules = mapBuilder.rules,
                difficulty = mapBuilder.difficulty,
                dimension = mapBuilder.dimension,
                lobby = mapBuilder.lobby,
                lobbyProperties = mapBuilder.lobbyProperties,
                region = mapBuilder.region,
                center = mapBuilder.center
        )
    }

}
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.spec.gventspec.gvent
import net.hungerstruck.renaissance.modules.region.*
import net.hungerstruck.renaissance.util.MapUtil
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.material.MaterialData
import org.bukkit.util.Vector

class GVent {
    fun gvent() = gvent {
		// Name of the GVent. This is how it will show up when using /event list, and how to execute the event using /event exe Meteor
        name = "Meteor"
		// Description of the GVent, what this will do
        desc = "Spawns a meteor at a random location"

		// Lambda function that will be ran when the GVent is executed
        executeFun =  { match: RMatch ->
            val point: Vector = match.map.mapBuilder.region.randomPoint()
            val region: CircleRegion = circle(point.blockX..point.blockZ, 5)

            // Inform the players how far away the meteor is
            for (player in match.alivePlayers) {
                point.y = player.location.y
                player.sendMessage("A meteor is falling ${point.distance(player.location.toVector()).toInt()} blocks from you!")
            }

            for (block in MapUtil.getBlocks(region)) {
                match.world.spawnFallingBlock(Location(match.world, block.blockX.toDouble(), 250.0, block.blockZ.toDouble()), MaterialData(Material.FIRE))
            }
        }
    }
}

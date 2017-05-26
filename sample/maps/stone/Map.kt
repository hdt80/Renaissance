import net.hungerstruck.renaissance.spec.mapspec.map
import net.hungerstruck.renaissance.spec.mapspec.rangeTo

// The name must be Map, or loading will not work
class Map {
	// The function must be map, or loading will not work
    fun map() = map {

		// Name of the map as it shows doing a /maps
        name = "Stone"
        version = "1.1"
        objective = "Be the last player standing"

		// Who made the map
        authors {
            - "HungerCraft Build Team"
        }

		// Which causes of sanity should be enabled. The options are:
		// 		SanityModule.Cause.HEIGHT		If the player is above the play limit
		//		SanityModule.Cause.CAVE			If the player is in a cave (below ground)
		//		SanityModule.Cause.LIGHT		If the player does not have enough light around them
		//		SanityModule.Cause.RADIUS		If the player is too far way from the center of the map
		//
		// By default all the causes are enabled
        sanity {
            enabledCauses = listOf()
        }

		// If thirst is enabled or not
        thirst {
            enabled = false
        }

		// Settings relating to TNT. Others options are:
		//		instantIgnite		If the TNT will blow up as soon as it is placed
		//		yield				Changes the default yielding of blocks in the explosion. Setting this to -1 disables this feature
		//		damageUnderWater	Currently does not function, but it would allow block damage underwater
        tnt {
            blockDamage = true
        }

        // Playable region
        boundary {
			// The center should be near the cornucopia
            center = (0..69..0)
			
			// Where in the map the players are allowed to move
            region = rectangle(min = (-256..-256), max = (256..256))
        }

		// Spawn points for the players. This can be as many as you'd like.
		// Players will spawn in order the pedestals are defined
        pedestals {
            - (11..70..-16)
            - (15..70..-12)
            - (18..70..-5)
            - (19..70..-2)
            - (19..70..2)
            - (18..70..5)
            - (16..70..11)
            - (11..70..16)
            - (6..70..18)
            - (2..70..19)
            - (-2..70..19)
            - (-6..70..18)
            - (-11..70..16)
            - (-16..70..11)
            - (-18..70..5)
            - (-19..70..2)
            - (-19..70..-2)
            - (-18..70..-5)
            - (-15..70..-12)
            - (-11..70..-16)
            - (-5..70..-18)
            - (-2..70..-19)
            - (2..70..-19)
            - (5..70..-18)
        }
    }
}

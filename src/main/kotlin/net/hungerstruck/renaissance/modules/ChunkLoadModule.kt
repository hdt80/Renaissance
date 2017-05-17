package net.hungerstruck.renaissance.modules

import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.spec.module.RModule
import net.hungerstruck.renaissance.spec.module.RModuleContext
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkUnloadEvent

class ChunkLoadModule(match: RMatch, modCtx: RModuleContext) : RModule(match, modCtx) {
    override fun init() {
        registerEvents()
    }

    @EventHandler
    fun onChunkUnload(event: ChunkUnloadEvent) {
        if(isMatch(event.chunk.world) && match.state != RMatch.State.PLAYING)
            event.isCancelled = true
    }
}

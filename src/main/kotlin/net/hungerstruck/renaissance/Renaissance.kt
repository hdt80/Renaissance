package net.hungerstruck.renaissance

import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.countdown.CountdownManager
import net.hungerstruck.renaissance.listeners.ConnectionListener
import net.hungerstruck.renaissance.listeners.LobbyListener
import net.hungerstruck.renaissance.listeners.SimpleEventsListener
import net.hungerstruck.renaissance.lobby.RLobbyManager
import net.hungerstruck.renaissance.match.RMatchManager
import net.hungerstruck.renaissance.modules.*
import net.hungerstruck.renaissance.modules.oregen.OregenModule
import net.hungerstruck.renaissance.modules.scoreboard.ScoreboardModule
import net.hungerstruck.renaissance.modules.ux.BloodModule
import net.hungerstruck.renaissance.modules.ux.ParticleModule
import net.hungerstruck.renaissance.modules.ux.SoundModule
import net.hungerstruck.renaissance.settings.Settings
import net.hungerstruck.renaissance.spec.gventspec.GVentContext
import net.hungerstruck.renaissance.util.ActionBarSender
import net.hungerstruck.renaissance.spec.mapspec.RMapContext
import net.hungerstruck.renaissance.spec.module.RModuleRegistry
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object Renaissance {
    var plugin: JavaPlugin? = null

    val mapContext: RMapContext = RMapContext()
    val gventContext: GVentContext = GVentContext()
    val matchManager: RMatchManager = RMatchManager()
    val lobbyManager: RLobbyManager
    val countdownManager: CountdownManager = CountdownManager()
    val eventManager: REventManager = REventManager()

    init {
        RModuleRegistry.register<ChestModule>()
        RModuleRegistry.register<ChatModule>()
        RModuleRegistry.register<DeathModule>()
        RModuleRegistry.register<PedestalModule>()
        RModuleRegistry.register<BoundaryModule>()
        RModuleRegistry.register<GameRuleModule>()
        RModuleRegistry.register<SanityModule>()
        RModuleRegistry.register<TimeLockModule>()
        RModuleRegistry.register<TimeSetModule>()
        RModuleRegistry.register<OregenModule>()
        RModuleRegistry.register<ThirstModule>()
        RModuleRegistry.register<ScoreboardModule>()
        RModuleRegistry.register<SoundModule>()
        RModuleRegistry.register<ParticleModule>()
        RModuleRegistry.register<BloodModule>()
        RModuleRegistry.register<ChunkLoadModule>()
        RModuleRegistry.register<TNTSettingsModule>()
        RModuleRegistry.register<SpecCallbackModule>()

        mapContext.loadMaps(File(RConfig.Maps.mapDir))
        gventContext.loadGVents(File(RConfig.GVents.gventDir))

        // Check there are maps loaded
        if (mapContext.getMaps().size == 0) {
            throw IllegalStateException("Must have at least one map to start loading Renaissance.")
        }

        // Check for at least 1 lobby
        if (mapContext.getMaps().filter { !it.isLobby }.isEmpty()) {
            throw IllegalStateException("Must have at least one lobby to start loading Renaissance.")
        }

        // Check for at least 1 map
        if (mapContext.getMaps().filter { it.isLobby }.isEmpty()) {
            throw IllegalStateException("Must have at least one game map to start loading Renaissance.")
        }

        lobbyManager = RLobbyManager(RConfig.Lobby.defaultLobby)
    }

    fun initialize(plugin: JavaPlugin) {
        this.plugin = plugin

        ActionBarSender.runTaskTimerAsynchronously(plugin, 5, 5)

        Bukkit.getPluginManager().registerEvents(RPlayer.Companion, plugin)
        Bukkit.getPluginManager().registerEvents(LobbyListener(), plugin)
        Bukkit.getPluginManager().registerEvents(ConnectionListener(), plugin)
        Bukkit.getPluginManager().registerEvents(SimpleEventsListener(), plugin)

        Settings.register()
    }
}
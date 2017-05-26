package net.hungerstruck.renaissance.commands

import com.sk89q.minecraft.util.commands.*
import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.rplayer
import net.hungerstruck.renaissance.spec.gventspec.GVent
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File

object GVentCommands {
    @JvmStatic
    @NestedCommand(GVentChildCommands::class)
    @CommandPermissions("renaissance.admin")
    @Command(
            aliases = arrayOf("gvent", "event", "ev"),
            desc = "GVent commands",
            usage = "[list]",
            min = 0,
            max = -1
    )
    fun gventParent(args: CommandContext, sender: CommandSender) {}

    object GVentChildCommands {
        @JvmStatic
        @Command(
                aliases = arrayOf("list", "ls"),
                desc = "List all the loaded GVents",
                usage = "",
                min = 0,
                max = 0
        )
        fun list(args: CommandContext, sender: CommandSender) {
            sender.sendMessage("${ChatColor.GOLD}Loaded events:")
            for (gvent: GVent in Renaissance.gventContext.getGVents()) {
                sender.sendMessage("${ChatColor.YELLOW}${gvent.gventInfo.name}")
            }
        }

        @JvmStatic
        @Command(
                aliases = arrayOf("execute", "exe"),
                desc = "Execute a gvent",
                usage = "<GVent name>",
                min = 1,
                max = -1
        )
        fun execute(args: CommandContext, sender: CommandSender) {
            val player: Player = CommandUtils.getPlayer(sender)

            if (player.rplayer.match == null) {
                player.sendMessage("${ChatColor.RED}You must be in a match to use this command")
                return
            }

            if (player.rplayer.match!!.state != RMatch.State.PLAYING) {
                player.sendMessage("${ChatColor.RED}The match must be running for an event to occur")
                return
            }

            val eventName: String = args.joinedStrings(0)
            val gvent: GVent? = Renaissance.gventContext.getGVentExact(eventName)

            if (gvent == null) {
                player.sendMessage("${ChatColor.RED}Invalid event name $eventName")
                return
            }

            gvent.gventBuilder.executeFun(player.rplayer.match!!)

        }

        @JvmStatic
        @Command(
                aliases = arrayOf("reload", "rl"),
                desc = "Reload the loaded GVents",
                usage = "",
                min = 0,
                max = 0
        )
        fun reload(args: CommandContext, sender: CommandSender) {
            Renaissance.gventContext.unloadGVents()
            Renaissance.gventContext.loadGVents(File(RConfig.GVents.gventDir))
            sender.sendMessage("Reloaded GVents")
        }

    }
}


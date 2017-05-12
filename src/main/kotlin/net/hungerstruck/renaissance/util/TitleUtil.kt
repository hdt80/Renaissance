package net.hungerstruck.renaissance.util;

import net.hungerstruck.renaissance.RPlayer
import net.minecraft.server.v1_11_R1.IChatBaseComponent
import net.minecraft.server.v1_11_R1.PacketPlayOutTitle
import net.minecraft.server.v1_11_R1.PlayerConnection
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer

object TitleUtil {
    fun sendTitle(player: RPlayer, title: String, subtitle: String, fadeIn: Int, stay: Int, fadeOut: Int ) {
        val craftplayer: CraftPlayer = player.bukkit as CraftPlayer
        val connection: PlayerConnection = craftplayer.handle.playerConnection
        val titleJSON: IChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"$title\"}")
        val subtitleJSON: IChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"$subtitle\"}")
        val titlePacket: PacketPlayOutTitle =  PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleJSON, fadeIn, stay, fadeOut)
        val subtitlePacket: PacketPlayOutTitle = PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleJSON)
        connection.sendPacket(titlePacket)
        connection.sendPacket(subtitlePacket)
    }
}
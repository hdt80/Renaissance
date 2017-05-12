package net.hungerstruck.renaissance

import com.sk89q.minecraft.util.commands.ChatColor
import org.bukkit.Bukkit
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Simple Logger wrapper
 */
object RLogger {
    fun print(tag: String, fmt: String, vararg vargs: Any) {
        Bukkit.getConsoleSender().sendMessage("[$tag] ${String.format(fmt, vargs)}")
    }

    fun info(fmt: String, vararg vargs: Any) {
        print("INFO", fmt, vargs)
    }
    fun warn(fmt: String, vararg vargs: Any) {
        print("WARN", fmt, vargs)
    }
    fun error(fmt: String, vararg vargs: Any) {
        print("ERROR", fmt, vargs)
    }
    fun debug(fmt: String, vararg vargs: Any) {
        print("DEBUG", fmt, vargs)
    }
}
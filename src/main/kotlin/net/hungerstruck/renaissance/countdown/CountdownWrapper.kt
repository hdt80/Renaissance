package net.hungerstruck.renaissance.countdown

import net.hungerstruck.renaissance.Renaissance
import org.bukkit.scheduler.BukkitRunnable

/**
 * Acts as a wrapper between a BukkitRunnable and a Countdown
 */
class CountdownWrapper(val countdown: Countdown) : BukkitRunnable() {
    // How many seconds are left
    var timeLeft: Int = -1

    /**
     * Start the Countdown
     *
     * @param seconds How many seconds the Countdown should wait for
     */
    fun start(seconds: Int) {
        assert(seconds > 0, { "Countdown has to have positive time." })
        assert(timeLeft == -1, { "Countdown already started." })

        this.timeLeft = seconds

        countdown.onStart(seconds)
        // Run every 20 ticks, assuming the server runs on 20 TPS.
        this.runTaskTimer(Renaissance.plugin, 0, 20)
    }

    /**
     * Cancel the Countdown by calling the onCancel and cancelling the BukkitRunnable
     */
    override fun cancel() {
        countdown.onCancel()
        super.cancel()
    }

    /**
     * Run the BukkitRunnable
     */
    override fun run() {
        if (timeLeft == 0) {
            countdown.onFinish()
            // Cancel the countdown. **NOTE**: This calls super.cancel(), not this.cancel().
            super.cancel()
        } else {
            countdown.onTick(timeLeft--)
        }
    }
}
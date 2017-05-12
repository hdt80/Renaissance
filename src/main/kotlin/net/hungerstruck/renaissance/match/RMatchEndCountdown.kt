package net.hungerstruck.renaissance.match

import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.countdown.Countdown

class RMatchEndCountdown(val match: RMatch) : Countdown() {
    override fun onTick(timeLeft: Int) {
        val status = RConfig.Match.endMessage.format(timeLeft)

        if (timeLeft % 10 == 0 || timeLeft <= 5) {
            match.sendMessage(status)
        }
    }

    override fun onFinish() {
        match.removeMatch()

        Renaissance.countdownManager.removeCountdown(this)
    }
}

